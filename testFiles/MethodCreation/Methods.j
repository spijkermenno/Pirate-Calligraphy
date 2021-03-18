.bytecode 55.0
.source Main.java
.class public main
.super java/lang/Object

.method public static testMethod1()Ljava/lang/String;
.limit stack 99
.limit locals 99
ldc "Method test 1"
astore 0
aload 0
areturn
.end method

.method public static testMethod2()V
.limit stack 99
.limit locals 99
ldc "Method test 2"
astore 0
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 0
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
return
.end method

.method protected static testMethod3()I
.limit stack 99
.limit locals 99
ldc "Method test 3"
astore 0
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 0
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_1
ireturn
.end method

.method private static testMethod4()D
.limit stack 99
.limit locals 99
ldc "Method test 4"
astore 0
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 0
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc2_w 2.0
dreturn
.end method

.method private static testMethod5(I)V
.limit stack 99
.limit locals 99
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 0
invokevirtual java/io/PrintStream/println(I)V
return
.end method

.method private static testMethod6(D)V
.limit stack 99
.limit locals 99
getstatic java/lang/System/out Ljava/io/PrintStream;
dload 0
invokevirtual java/io/PrintStream/println(D)V
return
.end method

.method private static testMethod7(Ljava/lang/String;)V
.limit stack 99
.limit locals 99
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 0
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

invokestatic main/testMethod1()[Ljava/lang/String
invokestatic main/testMethod2()V
invokestatic main/testMethod3()I
invokestatic main/testMethod4()D
iconst_1
invokestatic main/testMethod5(I)V
ldc2_w 1.0
invokestatic main/testMethod6(D)V
ldc "Hello"
invokestatic main/testMethod7([Ljava/lang/String;)V
return
.end method