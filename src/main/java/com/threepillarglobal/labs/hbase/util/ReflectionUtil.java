package com.threepillarglobal.labs.hbase.util;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ClassUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Some helper merthods with reflection
 */
//TODO migrate by calling setters/getters and not set/get the field value directly
public abstract class ReflectionUtil {

    /**
     * Obtain a byte[] representation of the value inside a field
     *
     * @param <T> Class type
     * @param field The Field object
     * @param t The object instance
     * @return field value as byte[]
     * @throws Exception
     */
    public static <T> byte[] getFieldValue(Field field, T t) throws Exception {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        //enums
        if (fieldType.isEnum()) {
            return field.get(t).toString().getBytes();
        }
        //primitives and wrappers
        try {
			String simpleName = fieldType.getSimpleName();

			if (simpleName.equals("String")) {
				return Bytes.toBytes((String) field.get(t));
			} else {
				if (simpleName.equals("boolean")) {
					return Bytes.toBytes(field.getBoolean(t));
				} else {
					if (simpleName.equals("Boolean")) {
						return Bytes.toBytes((Boolean) field.get(t));
					} else {
						if (simpleName.equals("byte")) {
							return new byte[] { field.getByte(t) };
						} else {
							if (simpleName.equals("Byte")) {
								return new byte[] { ((Byte) field.get(t))
										.byteValue() };
							} else {
								if (simpleName.equals("byte[]")) {
									return (byte[]) field.get(t);
								} else {
									if (simpleName.equals("char")) {
										return Bytes.toBytes(field.getChar(t));
									} else {
										if (simpleName.equals("Character")) {
											return Bytes
													.toBytes((Character) field
															.get(t)); // do not cast to int
										} else {
											if (simpleName.equals("short")) {
												return Bytes.toBytes(field
														.getShort(t));
											} else {
												if (simpleName.equals("Short")) {
													return Bytes
															.toBytes((Short) field
																	.get(t));
												} else {
													if (simpleName
															.equals("int")) {
														return Bytes
																.toBytes(field
																		.getInt(t));
													} else {
														if (simpleName
																.equals("Integer")) {
															return Bytes
																	.toBytes((Integer) field
																			.get(t));
														} else {
															if (simpleName
																	.equals("long")) {
																return Bytes
																		.toBytes(field
																				.getLong(t));
															} else {
																if (simpleName
																		.equals("Long")) {
																	return Bytes
																			.toBytes((Long) field
																					.get(t));
																} else {
																	if (simpleName
																			.equals("float")) {
																		return Bytes
																				.toBytes(field
																						.getFloat(t));
																	} else {
																		if (simpleName
																				.equals("Float")) {
																			return Bytes
																					.toBytes((Float) field
																							.get(t));
																		} else {
																			if (simpleName
																					.equals("double")) {
																				return Bytes
																						.toBytes(field
																								.getDouble(t));
																			} else {
																				if (simpleName
																						.equals("Double")) {
																					return Bytes
																							.toBytes((Double) field
																									.get(t));
																				} else {
																					if (simpleName
																							.equals("BigDecimal")) {
																						return Bytes
																								.toBytes((BigDecimal) field
																										.get(t));
																					} else {
																						if (simpleName
																								.equals("Date")) {
																							return Bytes
																									.toBytes(((Date) field
																											.get(t))
																											.getTime());
																						} else {
																							if (simpleName
																									.equals("Timestamp")) {
																								return Bytes
																										.toBytes(((Date) field
																												.get(t))
																												.getTime());
																							} else {
																								// for
																								// all
																								// the
																								// rest
																								// do
																								// a
																								// json
																								// marshalling
																								Object o = field
																										.get(t);
																								if (o != null) {
																									ByteArrayOutputStream baos = new ByteArrayOutputStream();
																									ObjectMapper mapper = new ObjectMapper();
																									mapper.writeValue(
																											baos,
																											o);
																									return baos
																											.toByteArray();
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
        } catch (NullPointerException npe) {
        }
        return null;
        //return new byte[]{};
    }

    /**
     * Set a Field in an object given the byte[] representation of it's value
     *
     * @param <T> Class type
     * @param field The Field object
     * @param t The object instance
     * @param value field value to be set as byte[]
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> void setFieldValue(Field field, T t, byte[] value) throws Exception {
        if (value == null) {
            return;
        }
        field.setAccessible(true);
        @SuppressWarnings("rawtypes")
        Class fieldType = field.getType();
        //enums
        if (fieldType.getGenericSuperclass() != null && fieldType.getGenericSuperclass().toString().indexOf("java.lang.Enum") == 0) {
            field.set(t, Enum.valueOf(fieldType, new String(value)));
            return;
        }
        //primitives or wrappers
        String simpleName = fieldType.getSimpleName();
        
		if (simpleName.equals("String")) {
			field.set(t, Bytes.toString(value));
			return;
		} else {
			if (simpleName.equals("boolean")) {
				field.setBoolean(t, Bytes.toBoolean(value));
				return;
			} else {
				if (simpleName.equals("Boolean")) {
					field.set(t, Bytes.toBoolean(value));
					return;
				} else {
					if (simpleName.equals("byte")) {
						System.out.println("Size:" + value.length);
						byte val = value.length > 0 ? (byte) (value[0] & 0xFF)
								: 0;
						field.setByte(t, val);
						return;
					} else {
						if (simpleName.equals("Byte")) {
							byte val = value.length > 0 ? (byte) (value[0] & 0xFF)
									: 0;
							field.set(t, new Byte(val));
							return;
						} else {
							if (simpleName.equals("byte[]")) {
								field.set(t, value);
								return;
							} else {
								if (simpleName.equals("char")) {
									field.setChar(
											t,
											new Character((char) Bytes
													.toInt(value)));
									return;
								} else {
									if (simpleName.equals("Character")) {
										field.set(
												t,
												new Character((char) Bytes
														.toInt(value)));
										return;
									} else {
										if (simpleName.equals("short")) {
											field.setShort(t,
													Bytes.toShort(value));
											return;
										} else {
											if (simpleName.equals("Short")) {
												field.set(
														t,
														new Short(Bytes
																.toShort(value)));
												return;
											} else {
												if (simpleName.equals("int")) {
													field.setInt(t,
															Bytes.toInt(value));
													return;
												} else {
													if (simpleName
															.equals("Integer")) {
														field.set(
																t,
																new Integer(
																		Bytes.toInt(value)));
														return;
													} else {
														if (simpleName
																.equals("long")) {
															field.setLong(
																	t,
																	Bytes.toLong(value));
															return;
														} else {
															if (simpleName
																	.equals("Long")) {
																field.set(
																		t,
																		new Long(
																				Bytes.toLong(value)));
																return;
															} else {
																if (simpleName
																		.equals("float")) {
																	field.setFloat(
																			t,
																			Bytes.toFloat(value));
																	return;
																} else {
																	if (simpleName
																			.equals("Float")) {
																		field.set(
																				t,
																				new Float(
																						Bytes.toFloat(value)));
																		return;
																	} else {
																		if (simpleName
																				.equals("double")) {
																			field.setDouble(
																					t,
																					Bytes.toDouble(value));
																			return;
																		} else {
																			if (simpleName
																					.equals("Double")) {
																				field.set(
																						t,
																						new Double(
																								Bytes.toDouble(value)));
																				return;
																			} else {
																				if (simpleName
																						.equals("BigDecimal")) {
																					field.set(
																							t,
																							Bytes.toBigDecimal(value));
																					return;
																				} else {
																					if (simpleName
																							.equals("Date")) {
																						field.set(
																								t,
																								new Date(
																										Bytes.toLong(value)));
																						return;
																					} else {
																						if (simpleName
																								.equals("Timestamp")) {
																							field.set(
																									t,
																									new Timestamp(
																											Bytes.toLong(value)));
																							return;
																						} else {
																							if (value.length > 2) { // at
																													// least
																													// {}
																													// from
																													// json
																								ObjectMapper mapper = new ObjectMapper();
																								Object o = mapper
																										.readValue(
																												value,
																												0,
																												value.length,
																												fieldType);
																								field.set(
																										t,
																										o);
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
           
        
        
    }

    public static <T> T instantiate(Class<T> cls) throws Exception {
        // Create instance of the given class                
        @SuppressWarnings("unchecked")
        final Constructor<T> constr = (Constructor<T>) cls.getConstructors()[0];
        final List<Object> params = new ArrayList<Object>();
        for (Class<?> pType : constr.getParameterTypes()) {
            params.add((pType.isPrimitive()) ? ClassUtils.primitiveToWrapper(pType).newInstance() : null);
        }
        return constr.newInstance(params.toArray());
    }

    public static <T> T instantiate(Class<T> cls, Map<String, ? extends Object> args) throws Exception {
        final T instance = instantiate(cls);
        // Set separate fields
        for (Map.Entry<String, ? extends Object> arg : args.entrySet()) {
            Field f = cls.getDeclaredField(arg.getKey());
            f.setAccessible(true);
            f.set(instance, arg.getValue());
        }
        return instance;
    }
}
