package org.arabidopsis.ahocorasick;

/**
 * A map from bytes to objects.
 * The rule of this class is THOU SHALT NOT USE MEMORY
 */
public class TinyByteMap<T>{
  static final byte[] EMPTY = new byte[0];


  // Oh what a hack
  // if the map has size 0 keys is null, values is null
  // if the map has size 1 keys is a Byte, values is a T
  // else keys is byte[] and values is an Object[] of the same size
  // carrying the bytes in parallel
  private Object keys;
  private Object values;

  public T get(byte key){
    if(keys == null) return null;
    if(keys instanceof Byte){      
      return ((Byte)keys).byteValue() == key ? (T)values : null; 
    }
    byte[] keys = (byte[])this.keys;
    for(int i = 0; i < keys.length; i++){
      if(keys[i] == key) return (T)((Object[])values)[i];
    }
    return null; 
  }

  public void put(byte key, T value){
    if(keys == null){
      keys = key;
      values = value;
      return;
    }

    if(keys instanceof Byte){
      if(((Byte)keys).byteValue() == key){
        values = value;
      } else {
        keys = new byte[]{((Byte)keys).byteValue(), key};
        values = new Object[]{values, value};
      }
      return;
    }

    byte[] keys = (byte[])this.keys;
    Object[] values = (Object[])this.values;

    for(int i = 0; i < keys.length; i++){
      if(keys[i] == key){
        values[i] = value;
        return;
      }
    }

    byte[] newkeys = new byte[keys.length + 1];
    for(int i = 0; i < keys.length; i++){
      newkeys[i] = keys[i]; 
    }
    newkeys[newkeys.length - 1] = key;
   
    Object[] newvalues = new Object[values.length + 1];
    for(int i = 0; i < values.length; i++){
      newvalues[i] = values[i]; 
    }
    newvalues[newvalues.length - 1] = value;

    this.keys = newkeys;
    this.values = newvalues;
  }

  public byte[] keys(){
    if(keys == null) return EMPTY; 
    if(keys instanceof Byte) return new byte[]{((Byte)keys).byteValue()};
    return ((byte[])keys);
  }

}
