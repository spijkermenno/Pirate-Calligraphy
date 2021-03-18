.bytecode 55.0
.source Main.java
.class public main
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "increasing for loop"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
iconst_0
istore 0

L1:
iload 0
iconst_10
if_icmpge L2
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 0
invokevirtual java/io/PrintStream/println(I)V
iinc 0 1
goto L1
L2:

return
.end method