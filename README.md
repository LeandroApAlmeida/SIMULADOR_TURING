<h3>Simulador de Máquina de Turing</h3>

<br>

Este programa, escrito em linguagem Java usando como interface gráfica de usuário (GUI) a biblioteca java.swing, implementa um simulador de máquina de Turing com múltiplas fitas.

Uma máquina de Turing M é uma 8-upla:

<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>M = (Σ, Q, δ, q0, F, V, β, ⊛)</b>

<br>

Onde:

<br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Σ:</b> Alfabeto de símbolos de entrada;

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Q:</b> Conjunto de estados possíveis da máquina;
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>δ:</b> Função de transição ou programa, tal que:
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>δ : Q × (Σ ∪ V ∪ {β , ⊛}) → Q × (Σ ∪ V ∪ {β , ⊛}) × {E, D}</b>
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>q0:</b> Estado inicial da máquina (q0 ∈ Q);
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>F:</b> Conjunto dos estados finais (F ⊂ Q);
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>V:</b> Alfabeto auxiliar;
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>β: </b> Símbolo especial branco;
  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>⊛:</b> Símbolo especial marcador de início da fita.

<br>

O simulador implementa um IDE para a programação da máquina multifitas, com editor de código e "debug" integrados.

O programa para a máquina de Turing deve ter a seguinte sintaxe:

<br>

```text

// Este programa verifica se um número binário é divisível por 3.
// 
// Exemplo:
//
// Entrada: 11
// Resultado: Aceita

[Descricao]

	Nome =  Número Binário divisível por 3


[Parametros]

	AlfabetoEntrada = {0, 1}
	AlfabetoAuxiliar = {}
	Estados = {q0, q1, q2, qAccept}
	EstadoInicial = q0
	EstadosTerminais = {qAccept}
	NumeroFitas = 1


[Programa]

	q0, 0 = q0, 0, D
	q0, 1 = q1, 1, D
	q1, 0 = q2, 0, D
	q1, 1 = q0, 1, D
	q2, 0 = q1, 0, D
	q2, 1 = q2, 1, D
	q0, _ = qAccept, _, P

```

<br>

Onde:

<br>

Dupla barra (//): Indica que o texto adiante é um comentário.

<br>

Seção <b>[Descricao]</b>: Descrição do programa. Contém os campos:

  * <b>Nome:</b> Nome do programa a ser exibido no IDE.

<br>

Seção <b>[Parametros]</b>: Parâmetros para a máquina de Turing multifitas. Contém os campos:

  * <b>AlfabetoEntrada:</b> Alfabeto de símbolos de entrada (<b>Σ:</b>);
    
  * <b>AlfabetoAuxiliar:</b> Alfabeto auxiliar (<b>V:</b>);

  * <b>Estados:</b> Conjunto de estados possíveis da máquina (<b>Q:</b>);
  
  * <b>EstadoInicial:</b>  Estado inicial da máquina (<b>q0:</b>);

  * <b>EstadosTerminais:</b> Conjunto dos estados finais (<b>F:</b>);

  * <b>NumeroFitas:</b> Número de fitas da máquina;

<br>

Seção <b>[Programa]</b>: Função de Transição: δ : Q × (Σ ∪ V ∪ {β , ⊛}) → Q × (Σ ∪ V ∪ {β , ⊛}) × {E, D}

<br>

https://github.com/user-attachments/assets/b1e1fa06-7567-4a97-820a-20cd18f648f6
