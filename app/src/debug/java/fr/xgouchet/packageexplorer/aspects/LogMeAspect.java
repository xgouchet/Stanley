package fr.xgouchet.packageexplorer.aspects;


import android.support.annotation.NonNull;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import fr.xgouchet.packageexplorer.annotations.LogMe;

import static fr.xgouchet.packageexplorer.aspects.LogUtils.appendArgs;
import static fr.xgouchet.packageexplorer.aspects.LogUtils.appendMethodName;
import static fr.xgouchet.packageexplorer.aspects.LogUtils.getTag;

/**
 * @author Xavier Gouchet
 */
@Aspect
public class LogMeAspect {

    @Pointcut("execution(@fr.xgouchet.packageexplorer.annotations.LogMe *.new(..))")
    public void annotatedConstructor() {
    }

    @Pointcut("execution(@fr.xgouchet.packageexplorer.annotations.LogMe * *(..))")
    public void annotatedMethod() {
    }


    @Before("(annotatedConstructor() || annotatedMethod()) && @annotation(logMe)")
    public void adviceBefore(JoinPoint jp, LogMe logMe) {
        int level = Log.DEBUG;
        if (logMe != null) level = logMe.value();

        Log.println(level, getTag(jp), prettySignatureIn(jp));
    }

    @After("annotatedConstructor() && @annotation(logMe)")
    public void adviceAfterConstructor(JoinPoint jp, LogMe logMe) {
        int level = Log.DEBUG;
        if (logMe != null) level = logMe.value();

        Log.println(level, getTag(jp), prettySignatureOut(jp, null));
    }

    @AfterReturning(value = "annotatedMethod() && @annotation(logMe)", returning = "ret")
    public void adviceAfter(JoinPoint jp, LogMe logMe, Object ret) {
        int level = Log.DEBUG;
        if (logMe != null) level = logMe.value();

        Log.println(level, getTag(jp), prettySignatureOut(jp, ret));
    }


    private String prettySignatureIn(@NonNull JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder();

        builder.append("⇥ ");

        appendMethodName(joinPoint, builder);
        appendArgs(joinPoint, builder);

        return builder.toString();
    }

    private String prettySignatureOut(@NonNull JoinPoint joinPoint, Object ret) {

        Signature signature = joinPoint.getSignature();

        StringBuilder builder = new StringBuilder();

        builder.append("↤ ");

        appendMethodName(joinPoint, builder);
        appendArgs(joinPoint, builder);

        if (signature instanceof MethodSignature) {
            Class returnType = ((MethodSignature) signature).getReturnType();

            if ((returnType != Void.TYPE) && (returnType != Void.class)) {
                builder.append(" = ").append(ret);
            }
        }

        return builder.toString();
    }


}
