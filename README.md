<h3>Simulador de Máquina de Turing</h3>

<br>

Este programa, escrito em linguagem Java, implementa um simulador de máquina de Turing com múltiplas fitas.

Uma máquina de Turing M é uma 8-upla:

<p style="margin-left: 40px;"><b>M = (Σ, Q, δ, q0, F, V, β, ) ⊛</b></p>

Onde:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Σ:</b> Alfabeto de símbolos de entrada;

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Q:</b> Conjunto de estados possíveis da máquina;
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>δ:</b> Função de transição ou programa, tal que:
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>δ : Q × (Σ ∪ V ∪ {β , ⊛}) → Q × (Σ ∪ V ∪ {β , ⊛}) × {E, D}</b>
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>q0:</b> Estado inicial da máquina (q0 ∈ Q);
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>F:</b> Conjunto dos estados finais (F ⊂ Q);
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>V:</b> Alfabeto auxiliar;
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>β: </b> Símbolo especial branco;
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>⊛:</b> Símbolo especial marcador de início da fita.

Execute os programas no diretório EXEMPLOS para testar, ou tente escrever seu próprio programa.

<br><br>

https://github.com/user-attachments/assets/b1e1fa06-7567-4a97-820a-20cd18f648f6
