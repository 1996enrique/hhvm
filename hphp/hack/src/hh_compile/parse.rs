// Copyright (c) Facebook, Inc. and its affiliates.
//
// This source code is licensed under the MIT license found in the
// LICENSE file in the "hack" directory of this source tree.

use crate::utils;
use aast_parser::{rust_aast_parser_types::Env as AastParserEnv, AastParser};
use anyhow::{Context, Result};
use ocamlrep::rc::RcOc;
use oxidized::relative_path::{self, RelativePath};
use parser_core_types::{
    indexed_source_text::IndexedSourceText, parser_env::ParserEnv, source_text::SourceText,
};
use rayon::prelude::*;
use std::{
    io::{stdin, BufRead, BufReader},
    path::PathBuf,
    sync::Arc,
};
use structopt::StructOpt;
use strum_macros::{Display, EnumString};

#[derive(StructOpt, Clone, Debug)]
#[structopt(no_version)] // don't consult CARGO_PKG_VERSION (Buck doesn't set it)
pub struct Opts {
    #[structopt(long)]
    thread_num: Option<usize>,

    #[structopt(default_value, long)]
    parser: Parser,
}

#[derive(Clone, Copy, Debug, Display, EnumString)]
enum Parser {
    Aast,
    Positioned,
    PositionedByRef,
    PositionedWithFullTrivia,
    DirectDecl,
}

impl std::default::Default for Parser {
    fn default() -> Self {
        Self::Positioned
    }
}

pub fn run(opts: Opts) -> Result<()> {
    let files = BufReader::new(stdin())
        .lines()
        .collect::<std::io::Result<Vec<_>>>()
        .context("could not read line from input file list")?
        .into_iter()
        .map(|l| PathBuf::from(l.trim()))
        .collect::<Vec<_>>();

    opts.thread_num.map_or((), |thread_num| {
        rayon::ThreadPoolBuilder::new()
            .num_threads(thread_num)
            .build_global()
            .unwrap();
    });

    files
        .par_iter()
        .try_for_each(|f| parse_file(opts.parser, f.clone()))
}

fn parse_file(parser: Parser, filepath: PathBuf) -> anyhow::Result<()> {
    let content = utils::read_file(&filepath)?;
    let ctx = &Arc::new((filepath, content));
    stack_limit::with_elastic_stack(|stack_limit| {
        let new_ctx = Arc::clone(ctx);
        let env = ParserEnv::default();
        let (filepath, content) = new_ctx.as_ref();
        let path = RelativePath::make(relative_path::Prefix::Dummy, filepath.clone());
        let source_text = SourceText::make(RcOc::new(path.clone()), content.as_slice());
        match parser {
            Parser::PositionedWithFullTrivia => {
                let stdout = std::io::stdout();
                let w = stdout.lock();
                let mut s = serde_json::Serializer::new(w);
                let arena = bumpalo::Bump::new();
                let src = IndexedSourceText::new(source_text);
                positioned_full_trivia_parser::parse_script_to_json(
                    &arena,
                    &mut s,
                    &src,
                    env,
                    Some(stack_limit),
                )?
            }
            Parser::PositionedByRef => {
                let arena = bumpalo::Bump::new();
                let (_, _, _) = positioned_by_ref_parser::parse_script(
                    &arena,
                    &source_text,
                    env,
                    Some(stack_limit),
                );
            }
            Parser::Positioned => {
                let (_, _, _) =
                    positioned_parser::parse_script(&source_text, env, Some(stack_limit));
            }
            Parser::DirectDecl => {
                let arena = bumpalo::Bump::new();
                let _ = direct_decl_parser::parse_decls(
                    Default::default(),
                    path,
                    content,
                    &arena,
                    Some(stack_limit),
                );
            }
            Parser::Aast => {
                let indexed_source_text = IndexedSourceText::new(source_text);
                let env = AastParserEnv::default();
                let _ = AastParser::from_text(&env, &indexed_source_text, Some(stack_limit));
            }
        }
        Ok(())
    })?
}
