section .bss
	a RESQ 1
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
				mov r10, 4
	mov r11, 2
		mov eax, r10d
	shr r10, 32
	mov edx, r10d
	div r11d
	mov r10d, eax

	mov QWORD[INTERNAL____CACHE + 0], r10

		mov r10, 5
	mov QWORD[INTERNAL____CACHE + 8], r10
	mov r10, QWORD [INTERNAL____CACHE + 8]
	mov QWORD [INTERNAL____CACHE + 16], r10
	mov r10, [INTERNAL____CACHE + 0]
	mov QWORD[INTERNAL____CACHE + 24], r10
	mov r10, QWORD [INTERNAL____CACHE + 24]
	mov QWORD [INTERNAL____CACHE + 32], r10
	mov r12, QWORD [INTERNAL____CACHE + 16]
	mov r13, QWORD [INTERNAL____CACHE + 32]
	add r12, r13

	mov QWORD [INTERNAL____CACHE + 40], r12
	mov r10, QWORD [INTERNAL____CACHE + 40]

		mov r10, 3
	mov QWORD[INTERNAL____CACHE + 48], r10
	mov r10, QWORD [INTERNAL____CACHE + 48]
	mov QWORD [INTERNAL____CACHE + 56], r10
	mov r10, [INTERNAL____CACHE + 40]
	mov QWORD[INTERNAL____CACHE + 64], r10
	mov r10, QWORD [INTERNAL____CACHE + 64]
	mov QWORD [INTERNAL____CACHE + 72], r10
	mov r12, QWORD [INTERNAL____CACHE + 56]
	mov r13, QWORD [INTERNAL____CACHE + 72]
	mov edx, 0
	mov eax, r12d
	mul r13d
	mov r12d, edx
	shl r12, 32
	add r12d, eax

	mov QWORD [INTERNAL____CACHE + 80], r12
	mov r10, QWORD [INTERNAL____CACHE + 80]

		mov r10, 1
	mov QWORD[INTERNAL____CACHE + 88], r10
	mov r10, QWORD [INTERNAL____CACHE + 88]
	mov QWORD [INTERNAL____CACHE + 96], r10
	mov r10, [INTERNAL____CACHE + 80]
	mov r11, 2
		mov edx, 0
	mov eax, r10d
	mul r11d
	mov r10d, edx
	shl r10, 32
	add r10d, eax

	mov QWORD[INTERNAL____CACHE + 104], r10
	mov r10, QWORD [INTERNAL____CACHE + 104]
	mov QWORD [INTERNAL____CACHE + 112], r10
	mov r12, QWORD [INTERNAL____CACHE + 96]
	mov r13, QWORD [INTERNAL____CACHE + 112]
	add r12, r13

	mov QWORD [INTERNAL____CACHE + 120], r12
	mov r10, QWORD [INTERNAL____CACHE + 120]
	mov QWORD [a], r10
	;3
	mov eax, [a]
	mov ebx, 43
	CMP eax, ebx
	JE COND_1
	mov r10, 0
	JMP COND_1END
COND_1:
	mov r10, 1
COND_1END:

CMP r10, 0
	JE COND_1_FALSE
COND_1_TRUE:
		;1
	mov r8, 1
	mov eax, [a]
	call printNumber
	call printNewLine

	JMP COND_1_FINAL_END
COND_1_FALSE:
	;1
	mov eax, 4
	mov ebx, 1
	mov ecx, str_1
	mov edx, 23
	int 0x80
COND_1_FINAL_END:
	;4
	mov eax, 1
	mov ebx, 0
	int 0x80


section .data
	const10 dd 10
	digits db 48,49,50,51,52,53,54,55,56,57
	new_line DB 10
	str_1 DB "compilation problem", 10, 0
