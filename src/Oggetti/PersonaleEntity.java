package Oggetti;

public class PersonaleEntity {
	private static PersonaleEntity medicoSelezionato = null;

	private int matricola;

	private String password;

	public static PersonaleEntity getMedico(){
		if(medicoSelezionato == null){
			new ErroreDialog("Impossibile procedere con l'operazione selezionata", true);
		}
		return medicoSelezionato;
	}

	public static void setMedico(PersonaleEntity medico){
		medicoSelezionato = medico;
	}
}