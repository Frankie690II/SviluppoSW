package ExternalComponentsInterface;

import Entity.PersonaleEntity;
import Entity.Prenotazione;
import Entity.Ricetta;
import MainScreen.ErroreDialog;
import Entity.PazienteEntity;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.util.ArrayList;
import java.util.Random;

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
            st = conn.prepareStatement("UPDATE Visita " +
                    "SET Diagnosi = ?, Referti = ?, Osservazioni = ? " +
                    " WHERE visita.Prenotazione_ID = ?");
            st.setString(1, dettagli[0]);
            st.setString(2, dettagli[1]);
            st.setString(3, dettagli[2]);
            st.setInt(4,visita.getId());
            st.execute();
        } catch (SQLException e) {
            new ErroreDialog(""+e);
        }
   }

    public void inserisciPaziente(PazienteEntity paziente) {
        try {
            //Prepare statement
            st = conn.prepareStatement("INSERT INTO Paziente (CF,Password,Nome,Cognome,Data_di_nascita,Telefono,Indirizzo_email) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
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
        if(prenotazione.getCodicePrestazione()==0){
            try {
                st = conn.prepareStatement("SELECT MAX(ID) FROM Ricovero");
                rs = st.executeQuery();
                int newID;
                if(rs.next()) {
                    newID = rs.getInt("MAX(ID)")+1;
                } else {
                    newID = 1;
                }

                st = conn.prepareStatement("INSERT INTO Ricovero(ID, Data_inizio, Data_fine, Paziente_CF) VALUES (?,?,?,?)");
                st.setInt(1, newID);
                st.setString(2, prenotazione.getDataOraAppuntamento().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                st.setString(3, "0000-00-00");
                st.setString(4, prenotazione.getPaziente().getCodiceFiscale());
                st.execute();

                st = conn.prepareStatement("INSERT INTO Effettua(Ricovero_ID, PersonaleMedico_ID) VALUES(?,?)");
                st.setInt(1,newID);
                st.setInt(2, prenotazione.getMedico().getMatricola());
                st.execute();

                return true;
            } catch (SQLException e){
                return false;
            }
        }
        try {
            //Prepare statement
            st = conn.prepareStatement("INSERT INTO Ricetta (Numero_ricetta, Paziente_CF) VALUES (?,? )");
            //Set fields
            st.setString(1,prenotazione.getCodiceRicetta());
            st.setString(2,prenotazione.getPaziente().getCodiceFiscale());
            st.execute();
            //Prepare statement
            st = conn.prepareStatement("SELECT MAX(ID) FROM prenotazione");
            rs = st.executeQuery();
            rs.next();
            int newID = rs.getInt("MAX(ID)")+1;
            //Prepare statement
            st = conn.prepareStatement("INSERT INTO Prenotazione (ID, Regime,Limite_massimo,Paziente_CF,FasciaOraria_Data_e_ora,Prestazione_ID, Ricetta_Numero_ricetta) VALUES ( ?, ?, ?, ?, ?, ?, ?)");
            // We first need to convert from LocalDateTime to String
            String formattedDateTime = prenotazione.getDataOraAppuntamento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            //Set field
            String regime;
            if(prenotazione.getRicetta().getRegime()==0){
                regime = "SSN";
            } else {
                regime = "ALPI";
            }
            st.setInt(1, newID);
            st.setString(2, regime);
            st.setString(3, prenotazione.getLimiteMassimo().format(DateTimeFormatter.ISO_LOCAL_DATE));
            st.setString(4, prenotazione.getPaziente().getCodiceFiscale());
            st.setString(5,formattedDateTime);
            st.setInt(6, prenotazione.getCodicePrestazione());
            st.setString(7,prenotazione.getCodiceRicetta());
            //Execute
            st.execute();
            //Preprare statemnt
            st = conn.prepareStatement("INSERT INTO visita(Prenotazione_ID, PersonaleMedico_ID) VALUES (?,?)");
            st.setInt(1,newID);
            st.setInt(2, prenotazione.getMedico().getMatricola());
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
                String formattedNewFasciaOraria = newFasciaOraria.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                int id=prenotazione.getId();
                st = conn.prepareStatement("UPDATE Prenotazione SET FasciaOraria_Data_e_ora=? WHERE ID=?");
                //Set field
                st.setString(1, formattedNewFasciaOraria);
                st.setInt(2, id);
                //Execute
                st.execute();
                //Prepare statement
                st = conn.prepareStatement("UPDATE Visita SET PersonaleMedico_ID=? WHERE Prenotazione_ID=?");
                //Set field
                st.setInt(1,prenotazione.getMedico().getMatricola());
                st.setInt(2,prenotazione.getId());
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
            //We need the current day
            LocalDate dateCurrent = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
            String timeStart="00:00:01";
            LocalTime timeStartDay = LocalTime.parse(timeStart, dateTimeFormatter);
            LocalDateTime dateTimeStart = LocalDateTime.of(dateCurrent, timeStartDay);
            //We format to DateTime Pattern
            String formattedDateTimeStart = dateTimeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            st = conn.prepareStatement("Select prenotazione.ID "+
                    "From prenotazione "+
            // st = conn.prepareStatement("SELECT Paziente.*, Prenotazione.Limite_massimo, prenotazione.Regime, Prenotazione.ID, Prenotazione.Ricetta_Numero_ricetta, Prenotazione.FasciaOraria_Data_e_ora, Prestazione.Nome AS Nome_Prestazione , prestazione.id AS Prestazione_ID, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
            //        "FROM PersonaleMedico,Prenotazione,Paziente,Prestazione,Eroga " +
                    "WHERE " +
                        "Prenotazione.FasciaOraria_Data_e_ora >= ? " +
                        "AND prenotazione.Paziente_CF=? " +
            //            "AND Paziente.CF=Prenotazione.Paziente_CF " +
            //            "AND Prenotazione.Prestazione_ID=Prestazione.ID " +
            //            "AND Prestazione.ID=Eroga.Prestazione_ID " +
            //            "AND Eroga.Prestazione_ID=PersonaleMedico.ID");
                        "");
            //Set field
            st.setString(1,formattedDateTimeStart);
            st.setString(2,cf);
            //Execute
            rs=st.executeQuery();
            List<Prenotazione> prenotazioni = new ArrayList<>();
            while(rs.next()) {
                prenotazioni.add(ottieniPrenotazione(rs.getInt(1)));
            }
            if (prenotazioni.isEmpty()) {
                return null;
            }
            return prenotazioni;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

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
            String timeStart="00:00:01";
            LocalTime timeStartDay = LocalTime.parse(timeStart, dateTimeFormatter);
            String timeEnd="23:59:59";
            LocalTime timeEndDay = LocalTime.parse(timeEnd, dateTimeFormatter);
            LocalDateTime dateTimeStart = LocalDateTime.of(dateCurrent, timeStartDay);
            LocalDateTime dateTimeEnd=LocalDateTime.of(dateCurrent, timeEndDay) ;
            //We format to DateTime Pattern
            String formattedDateTimeStart = dateTimeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedDateTimeEnd = dateTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            st = conn.prepareStatement("SELECT Paziente.*, Prenotazione.Limite_massimo, prenotazione.Regime, Prenotazione.ID, Prenotazione.Ricetta_Numero_ricetta, Prenotazione.FasciaOraria_Data_e_ora, Prestazione.Nome AS Nome_Prestazione ,  prestazione.id AS Prestazione_ID, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
                    "FROM Paziente,Prestazione,Prenotazione,PersonaleMedico,Visita " +
                    "WHERE " +
                        "Prenotazione.FasciaOraria_Data_e_ora >= ? " +
                        "AND Prenotazione.FasciaOraria_Data_e_ora <= ? " +
                        "AND PersonaleMedico.ID=? " +
                        "AND PersonaleMedico.ID=Visita.PersonaleMedico_ID " +
                        "AND Visita.Prenotazione_ID=Prenotazione.ID " +
                        "AND Prenotazione.Paziente_CF=Paziente.CF " +
                        "AND Prenotazione.Prestazione_ID=Prestazione.ID");
            //Set field
            st.setString(1,formattedDateTimeStart);
            st.setString(2,formattedDateTimeEnd);
            st.setInt(3,medico.getMatricola());
            //Execute
            rs=st.executeQuery();
            List<Prenotazione> prenotazioni = new ArrayList<>();
            while(rs.next()) {
                Prenotazione p = parserPrenotazioni(rs);
                prenotazioni.add(p);
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

            st = conn.prepareStatement("SELECT Paziente.*, Prenotazione.Limite_massimo, prenotazione.Regime, Prenotazione.ID, Prenotazione.Ricetta_Numero_ricetta, Prenotazione.FasciaOraria_Data_e_ora, Prestazione.Nome AS Nome_Prestazione ,  prestazione.id AS Prestazione_ID, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
                    "FROM Paziente,Prestazione,Prenotazione,PersonaleMedico,Visita,eroga " +
                    "WHERE " +
                        "Prenotazione.FasciaOraria_Data_e_ora >= ? " +
                        "AND Prenotazione.FasciaOraria_Data_e_ora <= ? " +
                        "AND Paziente.CF=Prenotazione.Paziente_CF " +
                        "AND Prenotazione.Prestazione_ID=Prestazione.ID " +
                        "AND Prestazione.ID=Eroga.Prestazione_ID " +
                        "AND Eroga.Prestazione_ID=PersonaleMedico.ID");
            //Set field
            st.setString(1,formattedDateTimeStart);
            st.setString(2,formattedDateTimeEnd);
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
            st = conn.prepareStatement("SELECT *, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
                    "FROM PersonaleMedico,Eroga,Prestazione " +
                    "WHERE " +
                        "PersonaleMedico.ID=Eroga.PersonaleMedico_ID " +
                        "AND Eroga.Prestazione_ID=Prestazione.ID " +
                        "AND Prestazione.ID=?");
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

/***  Codice per riempire di orari il database***


    public static void main(String[] args) {
        DatabaseInterface.getInstance().riempiOrari();
    }
    public void riempiOrari(){
        Random numGenerator = new Random();
        for(int i = 0; i < 800; i++){
            LocalDate d= LocalDate.now().plusDays(i);
            for(int j = 1; j <= 12; j+=1){
                LocalTime t = LocalTime.MIDNIGHT.plusHours(8).plusHours(Math.abs(numGenerator.nextInt()%10));
                try{
                    st= conn.prepareStatement("INSERT INTO esercita_durante(fasciaoraria_data_e_ora, personalemedico_id) VALUES (?,?)");
                    st.setString(1, LocalDateTime.of(d,t).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
                    st.setInt(2,j);
                    st.execute();
                }catch (SQLException e){

                }
            }
        }
    }

 // ***/

    public List<LocalDateTime> ottieniOrari(int prestazione, LocalDateTime limiteMassimo) {
        if(prestazione == 0){
            List<LocalDateTime> times = new ArrayList<>();
            LocalDateTime orario = LocalDateTime.of(LocalDate.now(), LocalTime.of(10,00));
            do{
                times.add(orario);
                orario = orario.plusDays(1);
            }while(orario.compareTo(limiteMassimo)<0);
            return times;
        }
        try {
            LocalDateTime safeTimeCondition=LocalDateTime.now().plusHours(24);
            //Prepare statement
            st = conn.prepareStatement("SELECT Esercita_durante.FasciaOraria_Data_e_ora\n" +
                    "FROM Esercita_durante,personalemedico, fasciaoraria, eroga " +
                    "WHERE (Esercita_Durante.FasciaOraria_Data_e_ora, personalemedico.ID  )not IN(\n" +
                            "SELECT esercita_durante.FasciaOraria_Data_e_ora, personalemedico.ID\n" +
                            "FROM Esercita_durante,Visita,PersonaleMedico,Eroga,Prestazione,Prenotazione,fasciaoraria\n" +
                    "        WHERE(\n" +
                                "Prestazione.ID=Eroga.Prestazione_ID \n" +
                                "AND Eroga.PersonaleMedico_ID=PersonaleMedico.ID \n" +
                                "AND esercita_durante.PersonaleMedico_ID = PersonaleMedico.ID\n" +
                                "AND Esercita_durante.FasciaOraria_Data_e_Ora = FasciaOraria.Data_e_ora\n" +
                                "AND Prenotazione.ID=Visita.Prenotazione_ID \n" +
                                "AND PersonaleMedico.ID=Visita.PersonaleMedico_ID \n" +
                    "           AND Prenotazione.FasciaOraria_Data_e_ora=Esercita_durante.FasciaOraria_Data_e_ora) \n" +
                    "   ) " +
                    "AND esercita_durante.FasciaOraria_Data_e_ora = FasciaOraria.Data_e_ora " +
                    "AND Esercita_durante.FasciaOraria_Data_e_ora <=? \n" +
                    "AND Esercita_durante.FasciaOraria_Data_e_ora >= ? \n" +
                    "AND esercita_durante.PersonaleMedico_ID = personalemedico.ID "+
                    "AND Eroga.PersonaleMedico_ID = personalemedico.ID "+
                    "AND eroga.Prestazione_ID = ? "+
                    "GROUP BY esercita_durante.FasciaOraria_Data_e_ora");
            //Set field
            String formattedDateTime = limiteMassimo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedSafeTimeCondition = safeTimeCondition.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            st.setString(1,formattedDateTime);
            st.setString(2,formattedSafeTimeCondition);
            st.setInt(3,prestazione);
            //Execute
            rs=st.executeQuery();
            List<LocalDateTime> times = new ArrayList<>();
            while(rs.next()) {
                times.add(rs.getObject("FasciaOraria_Data_e_ora",LocalDateTime.class));
                System.out.println(rs.getObject("FasciaOraria_Data_e_ora",LocalDateTime.class));
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
            String formattedSafeTimeCondition = safeTimeCondition.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int matricolaMedico=medico.getMatricola();
            //Prepare statement
            st = conn.prepareStatement("SELECT Esercita_durante.FasciaOraria_Data_e_ora " +
                    "FROM Esercita_durante,Visita,PersonaleMedico,Eroga,Prestazione,Prenotazione " +
                    "WHERE (Esercita_Durante.FasciaOraria_Data_e_ora, personalemedico.ID  )not IN(" +
                        "SELECT esercita_durante.FasciaOraria_Data_e_ora, personalemedico.ID " +
                        "FROM Esercita_durante,Visita,PersonaleMedico,Eroga,Prestazione,Prenotazione,fasciaoraria " +
                        "WHERE(" +
                            " Eroga.PersonaleMedico_ID=PersonaleMedico.ID " +
                            "AND esercita_durante.PersonaleMedico_ID = PersonaleMedico.ID " +
                            "AND Esercita_durante.FasciaOraria_Data_e_Ora = FasciaOraria.Data_e_ora " +
                            "AND Prenotazione.ID=Visita.Prenotazione_ID " +
                            "AND PersonaleMedico.ID=Visita.PersonaleMedico_ID " +
                            "AND Prenotazione.FasciaOraria_Data_e_ora=Esercita_durante.FasciaOraria_Data_e_ora) " +
                        ") " +
                        "AND PersonaleMedico.ID=? " +
                        "AND esercita_durante.PersonaleMedico_ID = personalemedico.ID " +
                        "AND Esercita_durante.FasciaOraria_Data_e_ora <= ? " +
                        "AND Prestazione.ID=Eroga.Prestazione_ID " +
                    "GROUP BY esercita_durante.FasciaOraria_Data_e_ora");
            //Set field
            st.setInt(1,matricolaMedico);
            st.setString(2,formattedSafeTimeCondition);
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
                st = conn.prepareStatement("SELECT *, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
                        "FROM PersonaleMedico " +
                        "WHERE " +
                            "ID=? " +
                            "AND Password = ?");
            } else {
                st = conn.prepareStatement("SELECT *, personaleamministrativo.Nome AS Nome_Personale ,personaleamministrativo.Cognome AS Cognome_Personale, personaleamministrativo.ID AS ID_Personale , personaleamministrativo.Password AS Password_Personale " +
                        "FROM PersonaleAmministrativo " +
                        "WHERE ID=? AND Password = ?");
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
            st = conn.prepareStatement("SELECT Paziente.*, Prenotazione.Limite_massimo, prenotazione.Regime, Prenotazione.ID, Prenotazione.Ricetta_Numero_ricetta, Prenotazione.FasciaOraria_Data_e_ora, Prestazione.Nome AS Nome_Prestazione , prestazione.id AS Prestazione_ID, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
                    "FROM Paziente,Eroga,Prestazione,Prenotazione,PersonaleMedico,Visita " +
                    "WHERE " +
                        "Prenotazione.ID=? " +
                        "AND Paziente.CF=Prenotazione.Paziente_CF " +
                        "AND Prenotazione.Prestazione_ID=Prestazione.ID " +
                        "AND Prestazione.ID=Eroga.Prestazione_ID " +
                        "AND visita.PersonaleMedico_ID=PersonaleMedico.ID " +
                    "GROUP BY prenotazione.ID");
            //Set field
            st.setInt(1, id);
            //Execute
            rs=st.executeQuery();
            if(rs.next()) {
                return parserPrenotazioni(rs);
            }
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return null;
    }

    public Prenotazione ottieniPrenotazioneSpostabile(int prestazione, LocalDateTime limiteTemporale) {
        List<PersonaleEntity> medici = ottieniListaMedici(prestazione);
        Prenotazione bestCandidate = null;
        for(PersonaleEntity m : medici){
            try{
                st = conn.prepareStatement("SELECT prenotazione.ID, prenotazione.Limite_massimo " +
                        "FROM prenotazione, visita, personalemedico " +
                        "WHERE " +
                            "visita.PersonaleMedico_ID = ?" +
                        "    AND visita.Prenotazione_ID = prenotazione.ID" +
                        "    AND prenotazione.FasciaOraria_Data_e_ora < ?" +
                        "ORDER BY prenotazione.Limite_massimo desc");
                st.setInt(1, m.getMatricola());
                st.setString(2, limiteTemporale.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                rs = st.executeQuery();
                if(rs.next()){
                    Prenotazione candidate = ottieniPrenotazione(rs.getInt(1));
                    if(bestCandidate == null){
                        bestCandidate = candidate;
                    } else {
                        if(bestCandidate.compareTo(candidate) < 0){
                            bestCandidate = candidate;
                        }
                    }
                }
            } catch (SQLException e) {
                new ErroreDialog(e);
            }
        }
        if(bestCandidate!=null) {
            List<LocalDateTime> orari = ottieniOrari(bestCandidate.getCodicePrestazione(), bestCandidate.getLimiteMassimo());
            if (orari != null && !orari.isEmpty()) {
                return bestCandidate;
            }
        }
        return null;
    }

    public boolean rimuoviPrenotazione(Prenotazione prenotazione) {
        try {
            st=conn.prepareStatement("DELETE FROM Visita WHERE Prenotazione_ID=?");
            st.setInt(1, prenotazione.getId());
            st.execute();
            st = conn.prepareStatement("DELETE FROM Prenotazione WHERE ID=?");
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
            List<String> prestazioniErogabili=new ArrayList<>();
            //Prepare statement
            st = conn.prepareStatement("SELECT * FROM Prestazione");
            //Execute
            rs=st.executeQuery();
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
            String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            st = conn.prepareStatement("SELECT Visita.*,Prestazione.Nome AS Nome_prestazione,PersonaleMedico.Nome,PersonaleMedico.Cognome " +
                    "FROM Visita,Paziente,Prenotazione,PersonaleMedico,Eroga,Prestazione " +
                    "WHERE " +
                        "Paziente.CF=? " +
                        "AND Paziente.CF=Prenotazione.Paziente_CF " +
                        "AND Prenotazione.ID=Visita.Prenotazione_ID " +
                        "AND Visita.PersonaleMedico_ID=PersonaleMedico.ID " +
                        "AND PersonaleMedico.ID=Eroga.PersonaleMedico_ID " +
                        "AND Eroga.Prestazione_ID =Prestazione.ID " +
                        "AND Prenotazione.FasciaOraria_Data_e_ora<=?");
            //Set field
            st.setString(1, cf);
            st.setString(2, formattedNow);
            //Execute
            rs=st.executeQuery();
            String storicoVisite = "\n";
            while(rs.next()) {
                storicoVisite += (String.format("Codice prenotazione: %d\nTipo di prestazione: %s\nMedico: %s %s\nDiagnosi: %s\nReferti: %s\nOsservazioni: %s\n-----------------------------\n", rs.getInt("Prenotazione_ID"), rs.getString("Nome_prestazione"), rs.getString("Nome"), rs.getString("Cognome"), rs.getString("Diagnosi"), rs.getString("Referti"), rs.getString("Osservazioni")));
            }
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
            st = conn.prepareStatement("SELECT PersonaleMedico.*, PersonaleMedico.Nome AS Nome_Personale ,PersonaleMedico.Cognome AS Cognome_Personale, PersonaleMedico.ID AS ID_Personale , PersonaleMedico.Password AS Password_Personale " +
                    "FROM PersonaleMedico,Esercita_durante,Eroga,Prestazione " +
                    "WHERE " +
                        "Prestazione.ID=? " +
                        "AND Esercita_durante.FasciaOraria_Data_e_ora=? " +
                        "AND Prestazione.ID=Eroga.Prestazione_ID " +
                        "AND Eroga.PersonaleMedico_ID=PersonaleMedico.ID " +
                        "AND PersonaleMedico.ID=Esercita_durante.PersonaleMedico_ID");
            //Set field
            String formattedSlotScelto = slotScelto.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            st.setInt(1, prestazione);
            st.setString(2, formattedSlotScelto);
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
            boolean duplicati;
            boolean usatoDaAltri;
            //Prepare statement
            String numeroRicetta=ricetta.getCodiceRicetta();
            st = conn.prepareStatement("SELECT * " +
                    "FROM Ricetta,Prenotazione,Prestazione " +
                    "WHERE " +
                        "Ricetta.Numero_ricetta=? " +
                        "AND  Ricetta.Numero_ricetta = Prenotazione.Ricetta_Numero_ricetta " +
                        "AND Prenotazione.Prestazione_ID =Prestazione.ID");
            //Set field
            st.setString(1, numeroRicetta);
            //Execute
            rs=st.executeQuery();
            duplicati = rs.next();

            st = conn.prepareStatement("SELECT *" +
                    "FROM Ricetta " +
                    "Where " +
                    "  Ricetta.Numero_ricetta = ? "+
                    "  AND  NOT ricetta.Paziente_CF = ?");
            st.setString(1, numeroRicetta);
            st.setString(2,PazienteEntity.getPaziente().getCodiceFiscale());
            rs = st.executeQuery();
            usatoDaAltri = rs.next();
            return duplicati || usatoDaAltri;
        }catch(SQLException ex) {
            new ErroreDialog(ex);
        }
        return true;
    }

    public String ottieniPosizioneMedico(PersonaleEntity medico){
        try {
            int medicoId=medico.getMatricola();
            //Prepare statement
            st=conn.prepareStatement("SELECT Ambulatorio.* " +
                    "FROM Ambulatorio,PersonaleMedico " +
                    "WHERE " +
                        "PersonaleMedico.ID=? " +
                        "AND PersonaleMedico.Ambulatorio_Nome=Ambulatorio.Nome");
            //Set field
            st.setInt(1, medicoId);
            //Execute
            rs=st.executeQuery();
            if(rs.next()) {
                return ("L'ambulatorio " + rs.getString("Nome") + " del reparto " + rs.getString("Reparto_Nome") + "\n");
            } else {
                return "N/A, chiedere al centro informazioni";
            }
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
                return PazienteEntity.createInstance(codiceFiscale,nome,cognome,dataDiNascita,indirizzoEmail, telefono, password);
        } catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    private PersonaleEntity parserPersonale(ResultSet queryResult) {
        try{
                String nome = queryResult.getString("Nome_Personale");
            String cognome = queryResult.getString("Cognome_Personale");
            String password = queryResult.getString("Password_Personale");
            int matricola = queryResult.getInt("ID_Personale");

            return new PersonaleEntity(matricola, nome, cognome, password);
        } catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    private Prenotazione parserPrenotazioni(ResultSet queryResult) {
        try{
            PazienteEntity paziente=parserPaziente(queryResult);
            PersonaleEntity medico=parserPersonale(queryResult);
            int id=queryResult.getInt("ID");
            int  idPrestazione=queryResult.getInt("Prestazione_ID");
            String descrizionePrestazione=queryResult.getString("Nome_Prestazione");

            //We format from String to LocalDateTime
            String str = queryResult.getString("FasciaOraria_Data_e_ora");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dataOraAppuntamento = LocalDateTime.parse(str, formatter);
            //We make a new Ricetta
            String codiceRicetta=queryResult.getString("Ricetta_Numero_ricetta");
            str = queryResult.getString("Limite_massimo");
            LocalDateTime limiteMassimo = LocalDateTime.parse(str +" 12:00:00", formatter);
            String regime=rs.getString("Regime");
            int regimeInt;
            if(regime.equals("SSN")){
                regimeInt=0;
            }else{
                regimeInt=1;
            }
            Ricetta ricetta=new Ricetta( codiceRicetta,  idPrestazione, limiteMassimo, regimeInt );

            Prenotazione prenotazione = new Prenotazione ( ricetta,  medico, id ,  paziente,  descrizionePrestazione,  dataOraAppuntamento);

            return prenotazione;
        } catch(Exception ex){
            return null;
        }
    }

}
