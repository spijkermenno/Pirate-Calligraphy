.bytecode 55.0
.source Main.java
.class public main
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "While loop always true"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
L1:
iconst_1
ifeq L2
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 7"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
goto L1
L2:

getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "While loop always calculation"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
L3:
ldc 20
ldc 40
ldc 2
irem
if_icmpne L5
ldc 1
goto L6
L5:
ldc 0
L6:
ifeq L4
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "test 8"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
goto L3
L4:

return
.end method
