package ExternalComponentsInterface;

import Oggetti.*;
import com.sun.javafx.binding.StringFormatter;
import jdk.vm.ci.meta.Local;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.util.ArrayList;

public class DatabaseInterface {
    private Connection conn;
    private PreparedStatement st;
    private ResultSet rs;

    private static final DatabaseInterface instance = new DatabaseInterface();

    public static DatabaseInterface getInstance() {
        return instance;
    }

    private DatabaseInterface(){
        try{
            //Connection to our local server: DO NOT TOUCH
            conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","prova");
        }catch(SQLException ex) {
            new ErroreDialog("Errore nella connessione, riprovare più tardi.");
        }
    }

    public void aggiornaDettagliVisita(Prenotazione visita, String [] dettagli) {
        try {
            st = conn.prepareStatement("UPDATE Visita SET Diagnosi = ?, Referti = ?, Osservazioni = ?  WHERE Prenotazione_ID = ?");
            st.setString(1, dettagli[0]);
            st.setString(2, dettagli[1]);
            st.setString(3, dettagli[2]);
            st.setInt(4,visita.getCodicePrestazione());
            st.execute();
        } catch (SQLException e) {
            new ErroreDialog(""+e);
        }
   }

    public void inserisciPaziente(PazienteEntity paziente) {
        try {
            //Prepare statement
            st = conn.prepareStatement("INSERT INTO Paziente (CF,Password,Nome,Cognome,Data_di_nascita,Telefono,Indirizzo_email) VALUES (?, ?, ?, ?, ?, ?, ?)");
            //Set field
            st.setString(1, paziente.getCodiceFiscale());
            st.setString(2, paziente.getPassword());
            st.setString(3, paziente.getNome());
            st.setString(4, paziente.getCognome());
            st.setString(5, paziente.getDataDiNascita().format(DateTimeFormatter.ISO_LOCAL_DATE));
            st.setString(6, paziente.getTelefono());
            st.setString(7, paziente.getIndirizzoMail());
            //Execute
            st.execute();
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
    }

  public boolean inserisciPrenotazione(Prenotazione prenotazione) {
        try {
            //Prepare statement
            st = conn.prepareStatement("INSERT INTO Prenotazione (ID,Regime,Limite_massimo,Paziente_CF,FasciaOraria_Data_e_ora,Prestazione_ID, Ricetta_Numero_ricetta) VALUES (?, ?, ?, ?, ?, ?)");
            // We first need to convert from LocalDateTime to String
            String formattedDateTime = prenotazione.getDataOraAppuntamento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            //Set field
            st.setString(1, prenotazione.getDescrizionePrestazione());
            st.setString(2, prenotazione.getLimiteMassimo().format(DateTimeFormatter.ISO_LOCAL_DATE));
            st.setString(3, prenotazione.getPaziente().getCodiceFiscale());
            st.setString(4,formattedDateTime);
            st.setInt(5, prenotazione.getCodicePrestazione());
            st.setString(6,prenotazione.getCodiceRicetta());
            //Execute
            st.execute();
            return true;
        }catch(SQLException ex) {
            new ErroreDialog(ex);

            return false;
        }
    }

    public boolean modificaPrenotazione(Prenotazione prenotazione) {
            try{
                //Prepare statement
                LocalDateTime newFasciaOraria=prenotazione.getDataOraAppuntamento();
                int id=prenotazione.getId();
                st = conn.prepareStatement("UPDATE Prenotazione SET FasciaOraria_Data_e_ora=newFasciaOraria WHERE ID=id");
                //Execute
                st.execute();
                return true;
            }catch(SQLException ex){
                new ErroreDialog(ex);
                return false;
            }
    }

    public String [] ottieniDettagliVisita(Prenotazione prenotazione) {
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT Diagnosi,Referti,Osservazioni FROM Visita WHERE Prenotazione_ID=?");
            //Set field
            st.setInt(1,prenotazione.getId());
            //Execute
            rs=st.executeQuery();
            String[] dettagli=new String[3];
            rs.next();
            dettagli[0]=rs.getString("Diagnosi");
            dettagli[1]=rs.getString("Referti");
            dettagli[2]=rs.getString("Osservazioni");
            return dettagli;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public List<String> ottieniDocumentiNecessari(int prestazione) {
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT Documentazione_Tipologia FROM Necessita WHERE Prestazione_ID=?");
            //Set field
            st.setInt(1,prestazione);
            //Execute
            rs=st.executeQuery();
            List<String> documenti = new ArrayList<String>();
            while(rs.next()) {
                documenti.add(rs.getString("Documentazione_Tipologia"));
            }
            return documenti;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public List<Prenotazione> ottieniElencoPrenotazioni(PazienteEntity paziente) {
        try{
            //Prepare statement
            String cf=paziente.getCodiceFiscale();
            st = conn.prepareStatement("SELECT Prenotazione.* FROM Prenotazione,Paziente WHERE Paziente.CF=cf AND Paziente.CF=Prenotazione.Paziente_CF");
            //Execute
            rs=st.executeQuery();
            List<Prenotazione> prenotazioni = new ArrayList<>();
            while(rs.next()) {
                prenotazioni.add(parserPrenotazioni(rs));
            }
            return prenotazioni;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    // This method is not yet used
    public List<Prenotazione> ottieniElencoVisite(PazienteEntity paziente) {
       return null;
    }

    public List<Prenotazione> ottieniElencoVisite(PersonaleEntity medico) {
        try{
            //Prepare statement
            int id=medico.getMatricola();
            //We need the current day
            LocalDate dateCurrent = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
            String timeStart="00:00:00";
            LocalTime timeStartDay = LocalTime.parse(timeStart, dateTimeFormatter);
            String timeEnd="23:59:59";
            LocalTime timeEndDay = LocalTime.parse(timeEnd, dateTimeFormatter);
            LocalDateTime dateTimeStart = LocalDateTime.of(dateCurrent, timeStartDay);
            LocalDateTime dateTimeEnd=LocalDateTime.of(dateCurrent, timeEndDay) ;
            //We format to DateTime Pattern
            String formattedDateTimeStart = dateTimeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedDateTimeEnd = dateTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            st = conn.prepareStatement("SELECT Prenotazione.* FROM Prenotazione,PersonaleMedico,Visita WHERE Prenotazione.FasciaOraria_Data_e_ora >= formattedDateTimeStart AND Prenotazione.FasciaOraria_Data_e_ora <= formattedDateTimeEnd AND PersonaleMedico.ID=id AND PersonaleMedico.ID=Visita.PersonaleMedico_ID AND Visita.Prenotazione_ID=Prenotazione.ID");
            //Execute
            rs=st.executeQuery();
            List<Prenotazione> prenotazioni = new ArrayList<>();
            while(rs.next()) {
                prenotazioni.add(parserPrenotazioni(rs));
            }
            return prenotazioni;
        }catch (SQLException ex){
            new ErroreDialog(ex);
        }
        return null;
    }

    public List<Prenotazione> ottieniElencoVisite(LocalDateTime inizio, LocalDateTime fine) {
        try{
            //Prepare statement
            String formattedDateTimeStart = inizio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedDateTimeEnd = fine.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            st = conn.prepareStatement("SELECT Prenotazione.* FROM Prenotazione WHERE Prenotazione.FasciaOraria_Data_e_ora >= inizio AND Prenotazione.FasciaOraria_Data_e_ora <= fine ");
            //Execute
            rs=st.executeQuery();
            List<Prenotazione> prenotazioni = new ArrayList<>();
            while(rs.next()) {
                prenotazioni.add(parserPrenotazioni(rs));
            }
            return prenotazioni;
        }catch (SQLException ex){
            new ErroreDialog(ex);
        }
        return null;
    }

    public List<PersonaleEntity> ottieniListaMedici(int prestazione) {
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT * FROM PersonaleMedico,Eroga,Prestazione WHERE PersonaleMedico.ID=Eroga.PersonaleMedico_ID AND Eroga.Prestazione_ID=Prestazione.ID AND Prestazione.ID=?");
            //Set field
            st.setInt(1,prestazione);
            //Execute
            rs=st.executeQuery();
            List<PersonaleEntity> personaleEntities = new ArrayList<>();
            while(rs.next()) {
                personaleEntities.add(parserPersonale(rs));
            }
            return personaleEntities;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public List<LocalDateTime> ottieniOrari(int prestazione, LocalDateTime limiteMassimo) {
        try {
            LocalDateTime safeTimeCondition=LocalDateTime.now().plusHours(24);
            //Prepare statement
            st = conn.prepareStatement("SELECT Esercita_durante.FasciaOraria_Data_e_ora FROM Esercita_durante,Visita,PersonaleMedico,Eroga,Prestazione,Prenotazione WHERE Esercita_durante.FasciaOraria_Data_e_ora <=? AND Prestazione.ID=? AND Esercita_durante.FasciaOraria_Data_e_ora <= safeTimeCondition AND Prestazione.ID=Eroga.Prestazione_ID AND Eroga.PersonaleMedico_ID=PersonaleMedico.ID AND NOT (Prenotazione.ID=Visita.Prenotazione_ID AND PersonaleMedico.ID=Visita.PersonaleMedico_ID AND Prenotazione.FasciaOraria_Data_e_ora=Esercita_durante.FasciaOraria_Data_e_ora)");
            //Set field
            String formattedDateTime = limiteMassimo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            st.setString(1,formattedDateTime);
            st.setInt(2,prestazione);
            //Execute
            rs=st.executeQuery();
            List<LocalDateTime> times = new ArrayList<>();
            while(rs.next()) {
                times.add(rs.getObject("FasciaOraria_Data_e_ora",LocalDateTime.class));
            }
            return times;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public List<LocalDateTime> ottieniOrari(PersonaleEntity medico) {
        try {
            LocalDateTime safeTimeCondition=LocalDateTime.now().plusHours(24);
            int matricolaMedico=medico.getMatricola();
            //Prepare statement
            st = conn.prepareStatement("SELECT Esercita_durante.FasciaOraria_Data_e_ora FROM Esercita_durante,Visita,PersonaleMedico,Eroga,Prestazione,Prenotazione WHERE PersonaleMedico.ID=matricolaMedico AND Esercita_durante.FasciaOraria_Data_e_ora <= safeTimeCondition AND Prestazione.ID=Eroga.Prestazione_ID AND Eroga.PersonaleMedico_ID=PersonaleMedico.ID AND NOT (Prenotazione.ID=Visita.Prenotazione_ID AND PersonaleMedico.ID=Visita.PersonaleMedico_ID AND Prenotazione.FasciaOraria_Data_e_ora=Esercita_durante.FasciaOraria_Data_e_ora)");
            //Execute
            rs=st.executeQuery();
            List<LocalDateTime> times = new ArrayList<>();
            while(rs.next()) {
                times.add(rs.getObject("FasciaOraria_Data_e_ora",LocalDateTime.class));
            }
            return times;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public PazienteEntity ottieniPaziente(String cf) {
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT * FROM Paziente WHERE CF=?");
            //Set field
            st.setString(1, cf);
            //Execute
            rs=st.executeQuery();
            if(rs.next()) {
                return parserPaziente(rs);
            }else {
                return null;
            }
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public PazienteEntity ottieniPaziente(String cf, String password) {
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT * FROM Paziente WHERE CF=? AND Password = ?");
            //Set field
            st.setString(1, cf);
            st.setString(2, password);
            //Execute
            rs=st.executeQuery();
            if(rs.next()) {
                return parserPaziente(rs);
            }else {
                return null;
            }
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public PersonaleEntity ottieniPersonale(String username, String password, boolean isMedico) {
        try {
            //Prepare statement
            if(isMedico) {
                st = conn.prepareStatement("SELECT * FROM PersonaleMedico WHERE ID=? AND Password = ?");
            } else {
                st = conn.prepareStatement("SELECT * FROM PersonaleAmministrativo WHERE ID=? AND Password = ?");
            }
            //Set field
            st.setString(1, username);
            st.setString(2, password);
            //Execute
            rs=st.executeQuery();
            if(rs.next()) {
                return parserPersonale(rs);
            }else {
                return null;
            }
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public Prenotazione ottieniPrenotazione(int id) {
        try {
            //Prepare statement
            st=conn.prepareStatement("SELECT * FROM Prenotazione WHERE Prenotazione.ID=id");
            //Execute
            rs=st.executeQuery();
            return parserPrenotazioni(rs);
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public Prenotazione ottieniPrenotazioneSpostabile(int prestazione, LocalDateTime limiteTemporale) {
        return null;
    }

    public boolean rimuoviPrenotazione(Prenotazione prenotazione) {
        try {
            st = conn.prepareStatement("DELETE FROM Prenotazione WHERE ID=?");
            st.setInt(1, prenotazione.getId());
            st.execute();
            st=conn.prepareStatement("DELETE FROM Visita WHERE Prenotazione_ID=?");
            st.setInt(1, prenotazione.getId());
            st.execute();
            return true;
        } catch (SQLException e) {
            new ErroreDialog(""+e);
            return false;
        }
    }

    public List<String> ottieniPrestazioniErogabili(){
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT * FROM Prestazione");
            //Execute
            rs=st.executeQuery();
            rs.next();
            List<String> prestazioniErogabili=new ArrayList<>();
            while(rs.next()) {
                int id=rs.getInt("ID");
                String idString=Integer.toString(id);
                prestazioniErogabili.add(idString + " - " + rs.getString("Nome"));
            }
            return prestazioniErogabili;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public String ottieniStoricoVisite(PazienteEntity paziente, LocalDateTime now) {
        try {
            //Prepare statement
            String cf= paziente.getCodiceFiscale();

            st = conn.prepareStatement("SELECT Visita.*,Prestazione.Nome,PersonaleMedico.Nome,PersonaleMedico.Cognome FROM Visita,Paziente,Prenotazione,PersonaleMedico,Eroga,Prestazione WHERE Paziente.CF=cf AND Paziente.CF=Prenotazione.Paziente_CF AND Prenotazione.ID=Visita.Prenotazione_ID AND Visita.PersonaleMedico_ID=PersonaleMedico.ID AND PersonaleMedico.ID=Eroga.PersonaleMedico_ID AND Eroga.Prestazione_ID =Prestazione.ID AND Prenotazione.FasciaOraria_Data_e_ora<=now");
            //Execute
            rs=st.executeQuery();
            rs.next();
            String storicoVisite=("ID: " + rs.getInt("ID") + "\nTipo di prestazione: " + rs.getString("Nome") +"\nMedico: " + rs.getString("PersonaleMedico.Nome") + " " + rs.getString("Cognome") + "\nDiagnosi: " + rs.getString("Diagnosi")+ "\nReferti: " + rs.getString("Referti") + "\nOsservazioni: " + rs.getString("Osservazioni") + "\n");
            /*
                "ID: " + ...+
                "\nTipo di prestazione: "+...
                "\nMedico: "+...
                "\nReferti: "+...
                   ...
                +"\n"
            */
            return storicoVisite;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public PersonaleEntity ottieniMedicoDisponibile(LocalDateTime slotScelto, int prestazione) {
        try {
            //Prepare statement
            st = conn.prepareStatement("SELECT PersonaleMedico.* FROM PersonaleMedico,Esercita_durante,Eroga,Prestazione WHERE Prestazione.ID=prestazione AND Esercita_durante.FasciaOraria_Data_e_ora=slotScelto AND Prestazione.ID=Eroga.Prestazione_ID AND Eroga.PersonaleMedico_ID=PersonaleMedico.ID AND PersonaleMedico.ID=Esercita_durante.PersonaleMedico_ID");
            //Execute
            rs=st.executeQuery();
            if(rs.next()) {
                return parserPersonale(rs);
            }else {
                return null;
            }
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public boolean verificaDuplicati(Ricetta ricetta) {
        try {
            //Prepare statement
            String numeroRicetta=ricetta.getCodiceRicetta();
            st = conn.prepareStatement("SELECT COUNT(Prenotazione.ID) FROM Ricetta,Prenotazione,Prestazione WHERE Ricetta.Numero_ricetta=numeroRicetta AND  Ricetta.Numero_ricetta = Prenotazione.Ricetta_Numero_ricetta AND Prenotazione.Prestazione_ID =Prestazione.ID");
            //Execute
            rs=st.executeQuery();
            rs.next();
            if(rs.getInt("COUNT(Prenotazione.ID)")>1){
                return true;
            }else{
                return false;
            }
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return false;
    }

    public String ottieniPosizioneMedico(PersonaleEntity medico){
        try {
            int medicoId=medico.getMatricola();
            //Prepare statement
            st=conn.prepareStatement("SELECT Ambulatorio.* FROM Ambulatorio,PersonaleMedico WHERE PersonaleMedico.ID=medicoId AND PersonaleMedico.Ambulatorio_Nome=Ambulatorio.Nome");
            //Execute
            rs=st.executeQuery();
            String posizioneMedico=("Nome ambulatorio: " + rs.getString("Nome") + "\nNome reparto: " + rs.getString("Reparto_Nome") + "\n");
            return posizioneMedico;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    private PazienteEntity parserPaziente(ResultSet queryResult) {
        try{
                String codiceFiscale=queryResult.getString("CF");
                String password = queryResult.getString("Password");
                String nome = queryResult.getString("Nome");
                String cognome = queryResult.getString("Cognome");
                LocalDate dataDiNascita = queryResult.getDate("Data_di_nascita").toLocalDate();
                String telefono = queryResult.getString("Telefono");
                String indirizzoEmail = queryResult.getString("Indirizzo_email");
                return PazienteEntity.createInstance(codiceFiscale,nome,cognome,dataDiNascita,telefono,indirizzoEmail, password);
        } catch(Exception ex){
            return null;
        }
    }

    private PersonaleEntity parserPersonale(ResultSet queryResult) {
        try{
                int matricola = queryResult.getInt("ID");
                String password = queryResult.getString("Password");
                return new PersonaleEntity(matricola, password);
        } catch(Exception ex){
            return null;
        }
    }

    private Prenotazione parserPrenotazioni(ResultSet queryResult) {
        try{
                PazienteEntity paziente=parserPaziente(queryResult);
                PersonaleEntity medico=parserPersonale(queryResult);
                medico.setMedico(medico);
                String codiceRicetta=queryResult.getString("Numero_ricetta");
                int prestazione=queryResult.getInt("Prestazione.ID");

                //We format from String to LocalDateTime
                String str = queryResult.getString("FasciaOraria_Data_e_ora");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime slotScelto = LocalDateTime.parse(str, formatter);
                return new Prenotazione( paziente,  new Ricetta(codiceRicetta,prestazione),  slotScelto,  medico);
        } catch(Exception ex){
            return null;
        }
    }

}
