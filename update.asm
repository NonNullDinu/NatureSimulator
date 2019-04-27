section .bss
	a resd 1
	b resd 1
	file_1_desc resw 1
	INTERNAL____CACHE RESQ 65536


;DO NOT EDIT
;THIS FILE IS COMPUTER GENERATED
;AS A RESULT OF THE COMPILATION OF "update.nsu"
section .text
print_char:
	push rax
	mov ecx, eax
	mov eax, 4
	mov ebx, r8d
	mov edx, 1
	int 0x80
	pop rax
	ret

printNumber:
	push rax
	push rdx
	xor edx,edx
	div dword[const10]
	test eax,eax
	je .l1
	call printNumber
.l1:
	lea eax,[digits+edx]
	call print_char
	pop rdx
	pop rax
	ret

printNewLine:
	mov eax, 4
	mov ebx, 1
	mov ecx, new_line
	mov edx, 1
	int 0x80
	ret


	global _start
_start:
	;1
	;2
mov r10, 43
	mov QWORD [a], r10
	;3
	mov r10, [a]
	mov r11, 4
	mov edx, 0
	mov eax, r10d
	mul r11d
	mov r10d, edx
	shl r10, 32
	add r10d, eax

	mov QWORD [INTERNAL____CACHE + 0], r10
mov QWORD [INTERNAL____CACHE + 8], 3
	mov r12, QWORD [INTERNAL____CACHE + 0]
	mov r13, QWORD [INTERNAL____CACHE + 8]
	mov eax, r12d
	shr r12, 32
	mov edx, r12d
	div r13d
	mov r12d, eax

	mov r10, r12
	mov QWORD [a], r10
	;4
	;5
	mov r10, [a]
	mov QWORD [INTERNAL____CACHE + 0], r10
	mov QWORD [b], r10
	;6
	mov r10, [a]
	mov r11, 2
	add r10, r11

	mov QWORD [INTERNAL____CACHE + 0], r10

	mov r10, [a]
	mov r11, [b]
	cmp r10, r11
	jne LOGIC_1_FALSE
	mov r10, 1
	JMP LOGIC_1_END
LOGIC_1_FALSE:
	 mov r10, 0
LOGIC_1_END:

	mov QWORD [INTERNAL____CACHE + 8], r10
	mov r10, [b]
	mov r11, QWORD [INTERNAL____CACHE + 0]
	cmp r10, r11
	jne LOGIC_2_FALSE
	mov r10, 1
	JMP LOGIC_2_END
LOGIC_2_FALSE:
	 mov r10, 0
LOGIC_2_END:

	mov QWORD [INTERNAL____CACHE + 16], r10
	mov r12, QWORD [INTERNAL____CACHE + 8]
	mov r13, QWORD [INTERNAL____CACHE + 16]
	and r12, r13
	and r12, 1

	mov QWORD [INTERNAL____CACHE + 24], r12

	mov r10, [a]
	mov r11, [b]
	cmp r10, r11
	je LOGIC_3_FALSE
	mov r10, 1
	JMP LOGIC_3_END
LOGIC_3_FALSE:
	 mov r10, 0
LOGIC_3_END:

	mov QWORD [INTERNAL____CACHE + 32], r10
	mov r10, QWORD [INTERNAL____CACHE + 24]
	mov QWORD [INTERNAL____CACHE + 40], r10
	mov r12, QWORD [INTERNAL____CACHE + 32]
	mov r13, QWORD [INTERNAL____CACHE + 40]
	or r12, r13
	and r12, 1

	mov QWORD [INTERNAL____CACHE + 48], r12

	mov r10, [a]
	mov r11, [b]
	cmp r10, r11
	jne LOGIC_4_FALSE
	mov r10, 1
	JMP LOGIC_4_END
LOGIC_4_FALSE:
	 mov r10, 0
LOGIC_4_END:

	mov QWORD [INTERNAL____CACHE + 56], r10

	mov r10, QWORD [INTERNAL____CACHE + 56]
	mov r11, 0
	cmp r10, r11
	je LOGIC_5_FALSE
	mov r10, 1
	JMP LOGIC_5_END
LOGIC_5_FALSE:
	 mov r10, 0
LOGIC_5_END:

	mov QWORD [INTERNAL____CACHE + 64], r10
	mov r10, QWORD [INTERNAL____CACHE + 48]
	mov QWORD [INTERNAL____CACHE + 72], r10
	mov r12, QWORD [INTERNAL____CACHE + 64]
	mov r13, QWORD [INTERNAL____CACHE + 72]
	xor r12, r13
	and r12, 1

	mov r10, r12

CMP r10, 0
	JE COND_0_FALSE
COND_0_TRUE:
	;7
	mov r8, 1
	mov eax, [a]
	call printNumber
	call printNewLine
	;8
	mov eax, 4
	mov ebx, 1
	mov ecx, str_1
	mov edx, 21
	int 0x80

	JMP COND_0_FINAL_END
COND_0_FALSE:
	;9
	mov eax, 4
	mov ebx, 2
	mov ecx, str_2
	mov edx, 21
	int 0x80
	;10
	mov eax, 1
	mov ebx, 0
	int 0x80
COND_0_FINAL_END:
	;11
	mov eax, 8
	mov ebx, file_1_path
	mov ecx, 0q644
	int 0x80
	mov [file_1_desc], eax
	mov eax, 4
	mov ebx, [file_1_desc]
	mov ecx, file_1_content
	mov edx, 11
	int 0x80
	mov eax, 6
	mov ebx, [file_1_desc]
	int 0x80
	;12
	mov eax, 1
	mov ebx, 0
	int 0x80


section .rodata
	const10 dd 10
	digits db 48,49,50,51,52,53,54,55,56,57
	new_line DB 10
	str_1 DB "compilation alright", 10, 0
	str_2 DB "compilation problem", 10, 0
	file_1_path DB "/home/dinu/eclipse-workspace/NS/ver_copy", 0
	file_1_content DB 49, 46, 51, 46, 50, 45, 97, 108, 112, 104, 97
