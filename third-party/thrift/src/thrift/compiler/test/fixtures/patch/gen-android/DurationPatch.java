/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.BitSet;
import java.util.Arrays;
import com.facebook.thrift.*;
import com.facebook.thrift.annotations.*;
import com.facebook.thrift.async.*;
import com.facebook.thrift.meta_data.*;
import com.facebook.thrift.server.*;
import com.facebook.thrift.transport.*;
import com.facebook.thrift.protocol.*;

/**
 * A patch for a Duration value.
 */
@SuppressWarnings({ "unused", "serial" })
public class DurationPatch implements TBase, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("DurationPatch");
  private static final TField ASSIGN_FIELD_DESC = new TField("assign", TType.STRUCT, (short)1);
  private static final TField CLEAR_FIELD_DESC = new TField("clear", TType.BOOL, (short)2);
  private static final TField ADD_FIELD_DESC = new TField("add", TType.STRUCT, (short)8);

  /**
   * Assign to a given value.
   * 
   * If set, all other patch operations are ignored.
   */
  public final DurationStruct assign;
  /**
   * Clear any set value.
   */
  public final Boolean clear;
  /**
   * Add to a given value.
   */
  public final DurationStruct add;
  public static final int ASSIGN = 1;
  public static final int CLEAR = 2;
  public static final int ADD = 8;

  public DurationPatch(
      DurationStruct assign,
      Boolean clear,
      DurationStruct add) {
    this.assign = assign;
    this.clear = clear;
    this.add = add;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DurationPatch(DurationPatch other) {
    if (other.isSetAssign()) {
      this.assign = TBaseHelper.deepCopy(other.assign);
    } else {
      this.assign = null;
    }
    if (other.isSetClear()) {
      this.clear = TBaseHelper.deepCopy(other.clear);
    } else {
      this.clear = null;
    }
    if (other.isSetAdd()) {
      this.add = TBaseHelper.deepCopy(other.add);
    } else {
      this.add = null;
    }
  }

  public DurationPatch deepCopy() {
    return new DurationPatch(this);
  }

  /**
   * Assign to a given value.
   * 
   * If set, all other patch operations are ignored.
   */
  public DurationStruct getAssign() {
    return this.assign;
  }

  // Returns true if field assign is set (has been assigned a value) and false otherwise
  public boolean isSetAssign() {
    return this.assign != null;
  }

  /**
   * Clear any set value.
   */
  public Boolean isClear() {
    return this.clear;
  }

  // Returns true if field clear is set (has been assigned a value) and false otherwise
  public boolean isSetClear() {
    return this.clear != null;
  }

  /**
   * Add to a given value.
   */
  public DurationStruct getAdd() {
    return this.add;
  }

  // Returns true if field add is set (has been assigned a value) and false otherwise
  public boolean isSetAdd() {
    return this.add != null;
  }

  @Override
  public boolean equals(Object _that) {
    if (_that == null)
      return false;
    if (this == _that)
      return true;
    if (!(_that instanceof DurationPatch))
      return false;
    DurationPatch that = (DurationPatch)_that;

    if (!TBaseHelper.equalsNobinary(this.isSetAssign(), that.isSetAssign(), this.assign, that.assign)) { return false; }

    if (!TBaseHelper.equalsNobinary(this.isSetClear(), that.isSetClear(), this.clear, that.clear)) { return false; }

    if (!TBaseHelper.equalsNobinary(this.isSetAdd(), that.isSetAdd(), this.add, that.add)) { return false; }

    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(new Object[] {assign, clear, add});
  }

  // This is required to satisfy the TBase interface, but can't be implemented on immutable struture.
  public void read(TProtocol iprot) throws TException {
    throw new TException("unimplemented in android immutable structure");
  }

  public static DurationPatch deserialize(TProtocol iprot) throws TException {
    DurationStruct tmp_assign = null;
    Boolean tmp_clear = null;
    DurationStruct tmp_add = null;
    TField __field;
    iprot.readStructBegin();
    while (true)
    {
      __field = iprot.readFieldBegin();
      if (__field.type == TType.STOP) {
        break;
      }
      switch (__field.id)
      {
        case ASSIGN:
          if (__field.type == TType.STRUCT) {
            tmp_assign = DurationStruct.deserialize(iprot);
          } else {
            TProtocolUtil.skip(iprot, __field.type);
          }
          break;
        case CLEAR:
          if (__field.type == TType.BOOL) {
            tmp_clear = iprot.readBool();
          } else {
            TProtocolUtil.skip(iprot, __field.type);
          }
          break;
        case ADD:
          if (__field.type == TType.STRUCT) {
            tmp_add = DurationStruct.deserialize(iprot);
          } else {
            TProtocolUtil.skip(iprot, __field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, __field.type);
          break;
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    DurationPatch _that;
    _that = new DurationPatch(
      tmp_assign
      ,tmp_clear
      ,tmp_add
    );
    _that.validate();
    return _that;
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.assign != null) {
      if (isSetAssign()) {
        oprot.writeFieldBegin(ASSIGN_FIELD_DESC);
        this.assign.write(oprot);
        oprot.writeFieldEnd();
      }
    }
    if (this.clear != null) {
      oprot.writeFieldBegin(CLEAR_FIELD_DESC);
      oprot.writeBool(this.clear);
      oprot.writeFieldEnd();
    }
    if (this.add != null) {
      oprot.writeFieldBegin(ADD_FIELD_DESC);
      this.add.write(oprot);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    return toString(1, true);
  }

  @Override
  public String toString(int indent, boolean prettyPrint) {
    return TBaseHelper.toStringHelper(this, indent, prettyPrint);
  }

  public void validate() throws TException {
    // check for required fields
  }

}

