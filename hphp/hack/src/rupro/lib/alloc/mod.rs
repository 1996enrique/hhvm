// Copyright (c) Meta Platforms, Inc. and affiliates.
//
// This source code is licensed under the MIT license found in the
// LICENSE file in the "hack" directory of this source tree.

mod alloc_bytes;
mod alloc_decl_defs;
mod alloc_pos;
mod alloc_reason;

use std::marker::PhantomData;

use lazy_static::lazy_static;

use crate::reason::{BReason, NReason, Reason};

/// For types which are not parameterized over Reason. Currently does not
/// allocate anything (bytes, strings, and paths are handled by the `intern`
/// crate, and allocated in its static tables for bytes and paths).
pub struct GlobalAllocator;

pub struct Allocator<R: Reason>(PhantomData<R>);

/// Get references to the allocator singletons. Please use this in the main
/// function only.
pub fn get_allocators_for_main() -> (
    &'static GlobalAllocator,
    &'static Allocator<NReason>,
    &'static Allocator<BReason>,
) {
    (&GLOBAL_ALLOC, &NO_REASON_ALLOC, &BOX_REASON_ALLOC)
}

lazy_static! {
    static ref GLOBAL_ALLOC: &'static GlobalAllocator = Box::leak(Box::new(GlobalAllocator::new()));
}

lazy_static! {
    static ref NO_REASON_ALLOC: &'static Allocator<NReason> =
        Box::leak(Box::new(Allocator::new(&GLOBAL_ALLOC)));
}

lazy_static! {
    static ref BOX_REASON_ALLOC: &'static Allocator<BReason> =
        Box::leak(Box::new(Allocator::new(&GLOBAL_ALLOC)));
}

impl GlobalAllocator {
    fn new() -> Self {
        Self
    }
}

impl<R: Reason> Allocator<R> {
    fn new(_global: &'static GlobalAllocator) -> Self {
        Self(PhantomData)
    }
}

impl std::fmt::Debug for GlobalAllocator {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("GlobalAllocator").finish()
    }
}

impl<T: Reason> std::fmt::Debug for Allocator<T> {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.debug_struct("Allocator").finish()
    }
}
