Este programa, escrito em linguagem Java, implementa um simulador de máquina de Turing com múltiplas fitas.

Uma máquina de Turing M é uma 8-upla:

  M = (Σ, Q, δ, q0, F, V, β, ) ⊛

Onde:

  Σ Alfabeto de símbolos de entrada;

  Q Conjunto de estados possíveis da máquina;
  
  δ Função de transição ou programa, tal que:
  
  δ : Q × (Σ ∪ V ∪ {β , ⊛}) → Q × (Σ ∪ V ∪ {β , ⊛}) × {E, D}
  
  q0 Estado inicial da máquina (q0 ∈ Q);
  
  F Conjunto dos estados finais (F ⊂ Q);
  
  V Alfabeto auxiliar;
  
  β Símbolo especial branco;
  
  ⊛ Símbolo especial marcador de início da fita.

Execute os programas no diretório EXEMPLOS para testar, ou tente escrever seu próprio programa.

<br><br>

https://github.com/user-attachments/assets/b1e1fa06-7567-4a97-820a-20cd18f648f6
