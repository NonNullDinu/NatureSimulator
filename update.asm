section .bss
	INTERNAL____READ RESB 19
	recind resq 1
	a resq 1
	b resq 1
	INTERNAL____CACHE RESQ 65536
	
	
	;DO NOT EDIT
	;THIS FILE IS COMPUTER GENERATED
	;AS A RESULT OF THE COMPILATION OF "update.nsu"
section .rodata
	const10 dd 10
	digits db 48,49,50,51,52,53,54,55,56,57
	new_line DB 10
	___end DB "Process finished execution and returned code "
	___end_len equ $-___end
	str_1 DB "compilation alright", 10, 0
	str_2 DB "compilation problem", 10, 0
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
	xor edx, edx
	div dword[const10]
	test eax, eax
	je .l1
	call printNumber
.l1:
	lea eax, [digits+edx]
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
readValue:
	mov eax, 3
	mov ebx, 0
	mov ecx, INTERNAL____READ
	mov edx, 18
	int 0x80
	mov ebx, eax
	sub ebx, 1
	mov r10, 0
	mov rax, 0
.l2:
	movzx rcx, BYTE [INTERNAL____READ + r10]
	sub rcx, '0'
	inc r10
	mul DWORD[const10]
	add rax, rcx
	cmp r10d, ebx
	jl .l2
	ret
	global _start
_start:
	mov QWORD [recind], 0
	mov QWORD [a], 43
	mov QWORD [INTERNAL____CACHE + 0], 43
	mov QWORD [b], 43
	mov r10, 43
	mov r11, 43
	cmp r10, 43
	jne .LOGIC_1_FALSE
	mov r10, 1
	jmp .LOGIC_1_END
.LOGIC_1_FALSE:
	mov r10, 0
.LOGIC_1_END:
	mov QWORD [INTERNAL____CACHE + 0], r10
	cmp QWORD [INTERNAL____CACHE + 0], 0
	jne .COND_1_TRUE
	mov r10, QWORD[a]
	mov QWORD [INTERNAL____CACHE + 8], r10
	mov r10, QWORD[b]
	add r10, 1
	mov QWORD [INTERNAL____CACHE + 16], r10
	mov r10, QWORD [INTERNAL____CACHE + 8]
	cmp r10, QWORD [INTERNAL____CACHE + 16]
	jne .LOGIC_2_FALSE
	mov r10, 1
	jmp .LOGIC_2_END
.LOGIC_2_FALSE:
	mov r10, 0
.LOGIC_2_END:
	mov QWORD [INTERNAL____CACHE + 16], r10
	mov r10, QWORD [INTERNAL____CACHE + 0]
	mov r11, QWORD [INTERNAL____CACHE + 16]
	or r10, r11
	and r10, 1
	cmp r10, 0
	je .COND_1_FALSE
.COND_1_TRUE:
	mov r8, 1
	mov rax, [a]
	call printNumber
	call printNewLine
	mov eax, 4
	mov ebx, 1
	mov ecx, str_1
	mov edx, 21
	int 0x80
	jmp .COND_1_FINAL_END
.COND_1_FALSE:;FORWARD_JUMP
	mov eax, 4
	mov ebx, 2
	mov ecx, str_2
	mov edx, 21
	int 0x80
	mov eax, 4
	mov ebx, 1
	mov ecx, ___end
	mov edx, ___end_len
	int 0x80
	mov rax, 1
	call printNumber
	call printNewLine
	mov eax, 1
	mov ebx, 1
	int 0x80
.COND_1_FINAL_END:;FORWARD JUMP
	push QWORD[a]
	call COUT
	pop r10
	mov eax, 4
	mov ebx, 1
	mov ecx, ___end
	mov edx, ___end_len
	int 0x80
	mov rax, 0
	call printNumber
	call printNewLine
	mov eax, 1
	mov ebx, 0
	int 0x80
COUT:;METHOD
	push rbp
	mov rbp, rsp
	sub rsp, 8
	lea r10, [rbp + 16]
	mov QWORD [rbp - 8], r10
	mov r10, QWORD[recind]
	add r10, 1
	mov QWORD [recind], r10
	mov r10, [rbp + 16]
	mov r11, 10
	mov eax, r10d
	mov edx, 0
	div r11d
	mov r10d, edx
	mov QWORD [INTERNAL____CACHE + 0], r10
	mov r11, 9
	cmp r10, 9
	je .LOGIC_3_FALSE
	mov r10, 1
	jmp .LOGIC_3_END
.LOGIC_3_FALSE:
	mov r10, 0
.LOGIC_3_END:
	cmp r10, 0
	je .COND_2_FINAL_END
.COND_2_TRUE:
	mov r10, [rbp + 16]
	add r10, 1
	mov r11, QWORD [rbp - 8]
	mov [r11], r10
.COND_2_FINAL_END:;FORWARD JUMP
	mov r10, [rbp + 16]
	mov r11, 9
	cmp r10, 9
	jle .LOGIC_4_FALSE
	mov r10, 1
	jmp .LOGIC_4_END
.LOGIC_4_FALSE:
	mov r10, 0
.LOGIC_4_END:
	cmp r10, 0
	je .COND_3_FINAL_END
.COND_3_TRUE:
	mov r10, [rbp + 16]
	mov r11, 10
	mov eax, r10d
	mov edx, 0
	div r11d
	mov r10d, eax
	push r10
	call COUT
	pop r10
.COND_3_FINAL_END:;FORWARD JUMP
	mov r10, [rbp + 16]
	mov r11, 10
	mov eax, r10d
	mov edx, 0
	div r11d
	mov r10d, edx
	lea eax, [digits + r10]
	call print_char
	mov r10, QWORD[recind]
	sub r10, 1
	mov QWORD [recind], r10
	mov r10, QWORD[recind]
	mov r11, 0
	cmp r10, 0
	jne .LOGIC_5_FALSE
	mov r10, 1
	jmp .LOGIC_5_END
.LOGIC_5_FALSE:
	mov r10, 0
.LOGIC_5_END:
	cmp r10, 0
	je .COND_4_FINAL_END
.COND_4_TRUE:
	call printNewLine
.COND_4_FINAL_END:;FORWARD JUMP
	leave
	ret
