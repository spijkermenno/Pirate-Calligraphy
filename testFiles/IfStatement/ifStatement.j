.bytecode 55.0
.source Main.java
.class public main
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "if statement calculation bigger than"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_40
iconst_2
irem
iconst_21
if_icmple L1
ldc 1
goto L2
L1:
ldc 0
L2:
ifeq L3
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 1"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 1
goto L4
L3:
ldc 0
L4:
ifne L5
goto L6
L5:
L6:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "if statement calculation smaller than"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_40
iconst_2
irem
iconst_21
if_icmpge L7
ldc 1
goto L8
L7:
ldc 0
L8:
ifeq L9
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 2"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 1
goto L10
L9:
ldc 0
L10:
ifne L11
goto L12
L11:
L12:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "if statement calculation equals"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_40
iconst_2
irem
iconst_21
if_icmpne L13
ldc 1
goto L14
L13:
ldc 0
L14:
ifeq L15
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 3"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 1
goto L16
L15:
ldc 0
L16:
ifne L17
goto L18
L17:
L18:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "if statement calculation not equals"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_40
iconst_2
irem
iconst_21
if_icmpeq L19
ldc 1
goto L20
L19:
ldc 0
L20:
ifeq L21
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 4"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 1
goto L22
L21:
ldc 0
L22:
ifne L23
goto L24
L23:
L24:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "if statement true"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_1
ifeq L25
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 5"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 1
goto L26
L25:
ldc 0
L26:
ifne L27
goto L28
L27:
L28:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "if statement false"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_0
ifeq L29
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 6"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 1
goto L30
L29:
ldc 0
L30:
ifne L31
goto L32
L31:
L32:
return
.end method
