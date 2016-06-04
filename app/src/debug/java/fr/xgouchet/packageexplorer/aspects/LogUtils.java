package fr.xgouchet.packageexplorer.aspects;

import android.support.annotation.NonNull;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author Xavier Gouchet
 */
public final class LogUtils {
    public static String getTag(@NonNull JoinPoint jp) {
        Object target = jp.getTarget();
        if (target == null){
            return jp.getSignature().getDeclaringType().getSimpleName();
        }
        return target.getClass().getSimpleName();
    }

    public static void appendMethodName(@NonNull JoinPoint joinPoint, @NonNull StringBuilder builder) {
        Signature signature = joinPoint.getSignature();

        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;

            builder.append(methodSignature.getReturnType().toString());
            builder.append(" ");
            builder.append(methodSignature.getName());

        } else if (signature instanceof ConstructorSignature) {
            builder.append("new ")
                    .append(signature.getDeclaringType().getSimpleName());

        }
    }

    public static void appendArgs(@NonNull JoinPoint joinPoint, @NonNull StringBuilder builder) {
        builder.append("(");

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; ++i) {
            if (i > 0) builder.append(", ");
            if (args[i] instanceof String) {
                builder.append('"').append(args[i]).append('"');
            } else {
                builder.append(args[i]);
            }
        }

        builder.append(")");
    }

    private LogUtils() {
    }
}
