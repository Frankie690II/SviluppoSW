package Oggetti;

import java.time.LocalDate;

public class PazienteEntity {
	private static PazienteEntity pazienteSelezionato = null;

	private String codiceFiscale;
	private String nome;
	private String cognome;
	private LocalDate dataDiNascita;
	private String indirizzoMail;
	private String telefono;
	private String password;

	private PazienteEntity(String codiceFiscale, String nome, String cognome, LocalDate dataDiNascita, String indirizzoMail, String telefono, String password) {
		this.setCodiceFiscale(codiceFiscale);
		this.setNome(nome);
		this.setCognome(cognome);
		this.setDataDiNascita(dataDiNascita);
		this.setIndirizzoMail(indirizzoMail);
		this.setTelefono(telefono);
		this.setPassword(password);
	}

	public static PazienteEntity createInstance(String codiceFiscale, String nome, String cognome, LocalDate dataDiNascita, String indirizzoMail, String telefono, String password){
        return new PazienteEntity(codiceFiscale, nome, cognome, dataDiNascita, indirizzoMail, telefono, password);
	}

    public static PazienteEntity getPaziente(){
		return pazienteSelezionato;
	}

	public static void setPaziente(PazienteEntity paziente){
		pazienteSelezionato = paziente;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}

	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public String getIndirizzoMail() {
		return indirizzoMail;
	}

	public void setIndirizzoMail(String indirizzoMail) {
		this.indirizzoMail = indirizzoMail;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
