Este programa, escrito em linguagem Java, implementa um simulador de máquina de Turing com múltiplas fitas.

Uma máquina de Turing M é uma 8-upla:

&nbsp;&nbsp;&nbsp;&nbsp; M = (Σ, Q, δ, q0, F, V, β, ) ⊛

Onde:

&nbsp;&nbsp;&nbsp;&nbsp; Σ Alfabeto de símbolos de entrada;

&nbsp;&nbsp;&nbsp;&nbsp; Q Conjunto de estados possíveis da máquina;
  
&nbsp;&nbsp;&nbsp;&nbsp; δ Função de transição ou programa, tal que:
  
&nbsp;&nbsp;&nbsp;&nbsp; δ : Q × (Σ ∪ V ∪ {β , ⊛}) → Q × (Σ ∪ V ∪ {β , ⊛}) × {E, D}
  
&nbsp;&nbsp;&nbsp;&nbsp; q0 Estado inicial da máquina (q0 ∈ Q);
  
&nbsp;&nbsp;&nbsp;&nbsp; F Conjunto dos estados finais (F ⊂ Q);
  
&nbsp;&nbsp;&nbsp;&nbsp; V Alfabeto auxiliar;
  
&nbsp;&nbsp;&nbsp;&nbsp; β Símbolo especial branco;
  
&nbsp;&nbsp;&nbsp;&nbsp; ⊛ Símbolo especial marcador de início da fita.

Execute os programas no diretório EXEMPLOS para testar, ou tente escrever seu próprio programa.

<br><br>

https://github.com/user-attachments/assets/b1e1fa06-7567-4a97-820a-20cd18f648f6
