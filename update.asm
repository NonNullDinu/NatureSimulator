section .bss
	c RESQ 1
	k RESQ 1
	j RESQ 1
	i RESQ 1
	INTERNAL____CACHE RESQ 65536


section .text
	global _start
_start:
	mov r10, 0
	mov [c], r10
	mov r10, 1000
	mov [k], r10
	mov r10, 0
	mov [j], r10
	mov r10, 0
	mov [i], r10
WHILE1:
	mov r10, [i]
	mov r11, 1
	add r10, r11
	mov [j], r10
WHILE2:
	INC WORD [c]
	INC WORD [j]
	mov eax, [j]
	mov ebx, [k]
	CMP eax, ebx
	JL COND_1
	mov r10, 0
	JMP COND_1END
COND_1:
	mov r10, 1
COND_1END:
	CMP r10, 0
	JNE WHILE2
	INC WORD [i]
	mov eax, [i]
	mov ebx, [k]
	CMP eax, ebx
	JL COND_2
	mov r10, 0
	JMP COND_2END
COND_2:
	mov r10, 1
COND_2END:
	CMP r10, 0
	JNE WHILE1
	mov eax, 4
	mov ebx, 1
	mov ecx, str_1
	mov edx, 16
	int 0x80
	mov r10, [c]
	mov ebx, str_2
	add ebx, 64
PARSE_1:
	movzx ax, r10w
	div 10
	movzx r10, al
	add ah, 48
	mov [ebx], ah
	sub ebx, 8
	CMP r10, 0
	JNE PARSE_1
	mov eax, 4
	mov ebx, 1
	mov ecx, str_2
	mov edx, 11
	int 0x80
	mov eax, 1
	mov ebx, 0
	int 0x80
	mov eax, 1
	mov ebx, 0
	int 0x80


section .data
	str_1 DB "this is a test", 10, 0
	str_2 DB "         ", 10, 0
