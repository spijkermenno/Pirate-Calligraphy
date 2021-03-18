.bytecode 55.0
.source Main.java
.class public main
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "Printing negative values"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc -5
invokevirtual java/io/PrintStream/println(I)V
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "Variabele declaratie"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
ldc 0
istore 1
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "adding and substracting one"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iinc 1 1
iinc 1 -1
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc ""
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iload 1
ldc 50
isub
istore 1
iload 1
ldc 49
iadd
istore 1
iload 1
ldc 20
if_icmpge L1
ldc 1
goto L2
L1:
ldc 0
L2:
ifeq L3
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "This shouldn't be printed"
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
iload 1
ldc 20
if_icmple L7
ldc 1
goto L8
L7:
ldc 0
L8:
ifeq L9
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "This should be printed"
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
iload 1
ldc 20
if_icmpne L13
ldc 1
goto L14
L13:
ldc 0
L14:
ifeq L15
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "This shouldn't be printed"
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
iload 1
ldc 20
if_icmpeq L19
ldc 1
goto L20
L19:
ldc 0
L20:
ifeq L21
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "This should be printed"
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
return
.end method
