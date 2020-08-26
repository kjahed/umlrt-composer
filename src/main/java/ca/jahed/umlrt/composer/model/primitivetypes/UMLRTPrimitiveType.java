package ca.jahed.umlrt.composer.model.primitivetypes;

import ca.jahed.umlrt.composer.model.UMLRTType;
import ca.jahed.umlrt.composer.model.primitivetypes.ansic.*;
import ca.jahed.umlrt.composer.model.primitivetypes.uml.*;
import org.eclipse.uml2.uml.Type;

public abstract class UMLRTPrimitiveType extends UMLRTType {
    public static UMLRTPrimitiveType get(Type type) {
        return get(type.getName());
    }

    public static UMLRTPrimitiveType get(String name) {
        switch (name) {
            case "Integer":
                return UMLRTInteger.get();
            case "Boolean":
                return UMLRTBoolean.get();
            case "Real":
                return UMLRTReal.get();
            case "String":
                return UMLRTString.get();
            case "UnlimitedNatural":
                return UMLRTUnlimitedNatural.get();
            case "bool":
                return UMLRTBool.get();
            case "char":
                return UMLRTChar.get();
            case "double":
                return UMLRTDouble.get();
            case "float":
                return UMLRTFloat.get();
            case "int":
                return UMLRTInt.get();
            case "int8_t":
                return UMLRTInt8.get();
            case "int16_t":
                return UMLRTInt16.get();
            case "int32_t":
                return UMLRTInt32.get();
            case "int64_t":
                return UMLRTInt64.get();
            case "long":
                return UMLRTLong.get();
            case "long double":
                return UMLRTLongDouble.get();
            case "short":
                return UMLRTShort.get();
            case "unsigned char":
                return UMLRTUnsignedChar.get();
            case "unsigned int":
                return UMLRTUnsignedInt.get();
            case "uint8_t":
                return UMLRTUnsignedInt8.get();
            case "uint16_t":
                return UMLRTUnsignedInt16.get();
            case "uint32_t":
                return UMLRTUnsignedInt32.get();
            case "uint64_t":
                return UMLRTUnsignedInt64.get();
            case "unsigned long":
                return UMLRTUnsignedLong.get();
            case "unsigned short":
                return UMLRTUnsignedShort.get();
            case "void":
                return UMLRTVoid.get();
            case "wchar_t":
                return UMLRTWChar.get();
            default:
                throw new RuntimeException("Unexpected type: "+name);
        }
    }
}
