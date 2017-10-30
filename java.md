# Technical Interview: Java

## Part 1 - Java Programming

### Please implement a small java program that takes a string of arbitrary length and returns the largest palindrome within the string that is greater than 1 character

From the root folder, run:

```bash
# Compile
javac src/com/example/interview/Interview.java
# Run
java -classpath src com.example.interview.Interview

# Compile Tests
javac -classpath "test;src;lib/hamcrest-core-1.3.jar;lib/junit-4.12.jar" test/com/example/interview/InterviewTest.java
# Run Tests
java -classpath "test;src;lib/hamcrest-core-1.3.jar;lib/junit-4.12.jar" org.junit.runner.JUnitCore com.example.interview.InterviewTest
```

## Part 2 - Stacktrace Debugging

A request for future people given this quiz, if you have the raw stacktrac.txt, that would be very nice. The PDF adds line breaks.

### 1. Give some examples of lines in the call trace that you think are part of the shareworks code base?

Anything containing "com.solium" is going to be in your company namespace, ex:
```
at com.solium.esoap.ejb.admin.employeegrantadmin.EmployeeGrantAdminBean.validateInBalance(EmployeeGrantAdminBean.java:7373)
at com.solium.esoap.ejb.admin.employeegrantadmin.EmployeeGrantAdminBean_qixr8e_EmployeeGrantAdminLocalImpl.validateInBalance(EmployeeGrantAdminBean_qixr8e_EmployeeGrantAdminLocalImpl.java:3208)
at com.solium.esoap.release.ejb.release.ReleaseAdminBean.getReleaseQuantityFactorDataAddPendingExercise(ReleaseAdminBean.java:2655)
at com.solium.esoap.release.ejb.release.ReleaseAdminBean.getReleaseQuantityFactorData(ReleaseAdminBean.java:2583)
at com.solium.esoap.release.ejb.release.ReleaseAdminBean_7iiyuo_ReleaseAdminLocalImpl.getReleaseQuantityFactorData(ReleaseAdminBean_7iiyuo_ReleaseAdminLocalImpl.java:2666)
```

### 2. Give some examples of lines in the call trace that you think are part of a framework or library that shareworks uses?

```
at sun.reflect.GeneratedMethodAccessor5960.invoke(Unknown Source)
at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
at java.lang.reflect.Method.invoke(Method.java:597)
at com.bea.core.repackaged.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:310)
at com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:182)
at com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:149)
at com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:131)
at com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:119)
at com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)
at com.bea.core.repackaged.springframework.jee.spi.MethodInvocationVisitorImpl.visit(MethodInvocationVisitorImpl.java:37)
at weblogic.ejb.container.injection.EnvironmentInterceptorCallbackImpl.callback(EnvironmentInterceptorCallbackImpl.java:54)
at com.bea.core.repackaged.springframework.jee.spi.EnvironmentInterceptor.invoke(EnvironmentInterceptor.java:50)
at com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)
at com.bea.core.repackaged.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:89)
at com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)
at com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.doProceed(DelegatingIntroductionInterceptor.java:131)
at com.bea.core.repackaged.springframework.aop.support.DelegatingIntroductionInterceptor.invoke(DelegatingIntroductionInterceptor.java:119)
at com.bea.core.repackaged.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:171)
at com.bea.core.repackaged.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:204)
```

### 3. Explain what do you think the com.solium.common.ejb.VerificationException exception class is used for?

It's used to indicate a verification exception in the Solium code. Something failed verification and the exception flowed all the way through the stack all the way up to the default error handler of weblogic.

### 4. Which method first threw the VerificationException? What information did it contain?

Method: com.solium.esoap.ejb.admin.employeegrantadmin.EmployeeGrantAdminBean.validateInBalance
Information: "The release quantity exceeds the number of units available for employee grant number 312"

### 5. What was the first non-framework method that was called during the execution that led up to this exception?

Method: com.solium.esoap.release.ejb.release.PreReleaseParticipantGainAndWithholdingReportAdminBean.getParticipantGainAndWithholdingReport
