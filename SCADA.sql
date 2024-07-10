-- *********************************************
-- * Standard SQL generation
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2
-- * Generator date: Sep 14 2021
-- * Generation date: Fri Jun 14 13:36:13 2024
-- * LUN file: D:\università\database\SCADA.lun
-- * Schema: SCHEMA/SQL
-- *********************************************


-- Database Section
-- ________________

create database SCADA;
use SCADA;

-- DBSpace Section
-- _______________


-- Tables Section
-- _____________

create table USR_ADDETTO (
     username varchar(32) not null,
     password varchar(128) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     regione varchar(21) not null,
     constraint ID_USR_ADDETTO_ID primary key (username));

create table COMP_MODELLO (
     aziendaComponente varchar(64) not null,
     codiceComponente varchar(16) not null,
     aziendaModello varchar(64) not null,
     nomeModello varchar(32) not null,
     constraint ID_COMP_MODELLO_ID primary key (aziendaModello, nomeModello, aziendaComponente, codiceComponente));

create table COMPONENTE (
     azienda varchar(64) not null,
     codice varchar(16) not null,
     descrizione varchar(1024) not null,
     msrp int not null,
     constraint ID_COMPONENTE_ID primary key (azienda, codice));

create table GARANZIA (
     azienda varchar(64) not null,
     nomeModello varchar(32) not null,
     durataAnni smallint not null,
     costo int not null,
     descrizione varchar(1024) not null,
     constraint ID_GARANZIA_ID primary key (azienda, nomeModello, durataAnni));

create table IMPIANTO (
     codiceImpianto int not null auto_increment,
     siglaProvincia char(2) not null,
     indirizzo varchar(128) not null,
     area float(1) not null,
     uomoInSito char not null,
     tipologia int not null,
     constraint ID_IMPIANTO_ID primary key (codiceImpianto, siglaProvincia));

create table MACC_STATUS (
     tipo int not null auto_increment,
     descrizione varchar(32) not null,
     constraint ID_MACC_STATUS_ID primary key (tipo));

create table MACCHINARIO (
     codiceInstallazione int not null auto_increment,
     dataInstallazione date not null,
     tipologia int not null,
     azienda varchar(64) not null,
     nomeModello varchar(32) not null,
     durataGaranzia smallint not null,
     status int not null,
     constraint ID_MACCHINARIO_ID primary key (codiceInstallazione));

create table INT_IMPIANTO (
     codiceIntervento int not null auto_increment,
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     constraint ID_INT_IMPIANTO_ID primary key (codiceIntervento, codiceImpianto, siglaProvincia));

create table INT_MACCHINARIO (
     codiceIntervento int not null auto_increment,
     codiceInstallazione int not null,
     constraint ID_INT_MACCHINARIO_ID primary key (codiceIntervento, codiceInstallazione));

create table INT_TIPO (
     tipo int not null auto_increment,
     descrizione varchar(32) not null,
     constraint ID_INT_TIPO_ID primary key (tipo));


create table INTERVENTO (
     codice int not null auto_increment,
     note varchar(1024),
     completato boolean not null default 0,
     usernameResponsabile varchar(32) not null,
     usernameTecnico varchar(32),
     tipo int not null,
     constraint ID_INTERVENTO_ID primary key (codice));

create table MACC_BIOGAS (
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     codiceInterno smallint not null,
     codiceInstallazione int not null,
     kwhOttimo float(1) not null,
     kgBatteri float(1) not null,
     kgUmido float(1) not null,
     constraint ID_MACC_BIOGAS_ID primary key (codiceImpianto, siglaProvincia, codiceInterno),
     constraint SID_MACC__INSTA_2_ID unique (codiceInstallazione));

create table MACC_EOLICO (
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     codiceInterno smallint not null,
     codiceInstallazione int not null,
     constraint ID_MACC_EOLICO_ID primary key (codiceImpianto, siglaProvincia, codiceInterno),
     constraint SID_MACC__INSTA_1_ID unique (codiceInstallazione));

create table MACC_FOTOVOLTAICO (
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     codiceInterno smallint not null,
     codiceInstallazione int not null,
     celle smallint not null,
     kwhMax float(1) not null,
     angolo float(1) not null,
     constraint ID_MACC_FOTOVOLTAICO_ID primary key (codiceImpianto, siglaProvincia, codiceInterno),
     constraint SID_MACC__INSTA_ID unique (codiceInstallazione));

create table MODELLO (
     azienda varchar(64) not null,
     nome varchar(32) not null,
     area float(1) not null,
     constraint ID_MODELLO_ID primary key (azienda, nome));

create table MONITORAGGIO (
     usernameAddetto varchar(32) not null,
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     constraint ID_MONITORAGGIO_ID primary key (usernameAddetto, codiceImpianto, siglaProvincia));

create table MACC_PRODUZIONE (
     codiceInstallazione int not null,
     ts timestamp not null default current_timestamp,
     kwh float(1) not null,
     constraint ID_MACC_PRODUZIONE_ID primary key (codiceInstallazione, ts));

create table PROVINCIA (
     sigla char(2) not null,
     regione varchar(21) not null,
     constraint ID_PROVINCIA_ID primary key (sigla));

create table USR_RESPONSABILE (
     username varchar(32) not null,
     password varchar(128) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     regione varchar(21) not null,
     constraint ID_USR_RESPONSABILE_ID primary key (username));

create table IMP_RILEVAZIONE (
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     ts timestamp not null default current_timestamp,
     vento float(1) not null,
     uv float(1) not null,
     constraint ID_IMP_RILEVAZIONE_ID primary key (codiceImpianto, siglaProvincia, ts));

create table MACC_EOLICO_SPECIFICHE (
     codiceImpianto int not null,
     siglaProvincia char(2) not null,
     codiceInterno smallint not null,
     nodi float(1) not null,
     kwh float(1) not null,
     constraint ID_MACC_EOLICO_SPECIFICHE_ID primary key (codiceImpianto, siglaProvincia, codiceInterno, nodi));

create table TIPOLOGIA (
     codice int not null auto_increment,
     Descrizione varchar(32) not null,
     constraint ID_TIPOLOGIA_ID primary key (codice));

create table USR_TECNICO (
     username varchar(32) not null,
     password varchar(128) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     siglaProvincia char(2) not null,
     constraint ID_USR_TECNICO_ID primary key (username));

-- Index Section
-- _____________

create unique index ID_USR_ADDETTO_IND
     on USR_ADDETTO (username);

create unique index ID_COMP_MODELLO_IND
     on COMP_MODELLO (aziendaModello, nomeModello, aziendaComponente, codiceComponente);

create index REF_COMP__COMPO_IND
     on COMP_MODELLO (aziendaComponente, codiceComponente);

create unique index ID_COMPONENTE_IND
     on COMPONENTE (azienda, codice);

create unique index ID_GARANZIA_IND
     on GARANZIA (azienda, nomeModello, durataAnni);

create unique index ID_IMPIANTO_IND
     on IMPIANTO (siglaProvincia, codiceImpianto);

create index REF_IMPIA_TIPOL_IND
     on IMPIANTO (tipologia);

create unique index ID_MACC_STATUS_IND
     on MACC_STATUS (tipo);

create unique index ID_MACCHINARIO_IND
     on MACCHINARIO (codiceInstallazione);

create index REF_INSTA_TIPOL_IND
     on MACCHINARIO (tipologia);

create index REF_INSTA_MODEL_IND
     on MACCHINARIO (azienda, nomeModello);

create index REF_INSTA_INST__IND
     on MACCHINARIO (status);

create unique index ID_INT_IMPIANTO_IND
     on INT_IMPIANTO (codiceIntervento, siglaProvincia, codiceImpianto);

create index REF_INT_I_IMPIA_IND
     on INT_IMPIANTO (siglaProvincia, codiceImpianto);

create unique index ID_INT_MACCHINARIO_IND
     on INT_MACCHINARIO (codiceIntervento, codiceInstallazione);

create index REF_INT_M_INSTA_IND
     on INT_MACCHINARIO (codiceInstallazione);

create unique index ID_INTERVENTO_IND
     on INTERVENTO (codice);

create index REF_INTER_RESPO_IND
     on INTERVENTO (usernameResponsabile);

create index REF_INTER_TECNI_IND
     on INTERVENTO (usernameTecnico);

create index REF_INTER_TIPO__IND
     on INTERVENTO (tipo);

create unique index ID_MACC_BIOGAS_IND
     on MACC_BIOGAS (siglaProvincia, codiceImpianto, codiceInterno);

create unique index SID_MACC__INSTA_2_IND
     on MACC_BIOGAS (codiceInstallazione);

create unique index ID_MACC_EOLICO_IND
     on MACC_EOLICO (siglaProvincia, codiceImpianto, codiceInterno);

create unique index SID_MACC__INSTA_1_IND
     on MACC_EOLICO (codiceInstallazione);

create unique index ID_MACC_FOTOVOLTAICO_IND
     on MACC_FOTOVOLTAICO (siglaProvincia, codiceImpianto, codiceInterno);

create unique index SID_MACC__INSTA_IND
     on MACC_FOTOVOLTAICO (codiceInstallazione);

create unique index ID_MODELLO_IND
     on MODELLO (azienda, nome);

create unique index ID_MONITORAGGIO_IND
     on MONITORAGGIO (usernameAddetto, codiceImpianto, siglaProvincia);

create index REF_MONIT_IMPIA_IND
     on MONITORAGGIO (codiceImpianto, siglaProvincia);

create unique index ID_MACC_PRODUZIONE_IND
     on MACC_PRODUZIONE (codiceInstallazione, ts);

create unique index ID_PROVINCIA_IND
     on PROVINCIA (sigla);

create unique index ID_USR_RESPONSABILE_IND
     on USR_RESPONSABILE (username);

create unique index ID_IMP_RILEVAZIONE_IND
     on IMP_RILEVAZIONE (siglaProvincia, codiceImpianto, ts);

create unique index ID_MACC_EOLICO_SPECIFICHE_IND
     on MACC_EOLICO_SPECIFICHE (codiceImpianto, siglaProvincia, codiceInterno, nodi);

create unique index ID_USR_TECNICO_IND
     on USR_TECNICO (username);

create index REF_TECNI_PROVI_IND
     on USR_TECNICO (siglaProvincia);

create unique index ID_TIPO_INTERVENTO_IND
     on INT_TIPO (tipo);

create unique index ID_TIPOLOGIA_IND
     on TIPOLOGIA (codice);

-- Constraints Section
-- ___________________

/*MySql does not support select clauses inside check() */
/*
alter table USR_ADDETTO add constraint ID_USR_ADDETTO_CHK
     check(exists(select * from MONITORAGGIO
                  where MONITORAGGIO.usernameAddetto = username));
*/

alter table COMP_MODELLO add constraint EQU_COMP__MODEL
     foreign key (aziendaModello, nomeModello)
     references MODELLO(azienda, nome);

alter table COMP_MODELLO add constraint REF_COMP__COMPO_FK
     foreign key (aziendaComponente, codiceComponente)
     references COMPONENTE(azienda, codice);

alter table GARANZIA add constraint REF_GARAN_MODEL
     foreign key (azienda, nomeModello)
     references MODELLO(azienda, nome);

alter table IMPIANTO add constraint REF_IMPIA_PROVI
     foreign key (siglaProvincia)
     references PROVINCIA(sigla);

alter table IMPIANTO add constraint REF_IMPIA_TIPOL_FK
     foreign key (tipologia)
     references TIPOLOGIA(codice);

alter table MACCHINARIO add constraint REF_INSTA_TIPOL_FK
     foreign key (tipologia)
     references TIPOLOGIA(codice);

alter table MACCHINARIO add constraint REF_INSTA_MODEL_FK
     foreign key (azienda, nomeModello)
     references MODELLO(azienda, nome);

alter table MACCHINARIO add constraint REF_INSTA_GARAN_FK
     foreign key (azienda, nomeModello, durataGaranzia)
     references GARANZIA(azienda, nomeModello, durataAnni);

alter table MACCHINARIO add constraint REF_INSTA_INST__FK
     foreign key (status)
     references MACC_STATUS(tipo);

alter table INT_IMPIANTO add constraint REF_INT_I_IMPIA_FK
     foreign key (codiceImpianto, siglaProvincia)
     references IMPIANTO(codiceImpianto, siglaProvincia);

alter table INT_IMPIANTO add constraint REF_INT_I_INTER
     foreign key (codiceIntervento)
     references INTERVENTO(codice);

alter table INT_MACCHINARIO add constraint REF_INT_M_INTER
     foreign key (codiceIntervento)
     references INTERVENTO(codice);

alter table INT_MACCHINARIO add constraint REF_INT_M_INSTA_FK
     foreign key (codiceInstallazione)
     references MACCHINARIO(codiceInstallazione);

alter table INTERVENTO add constraint REF_INTER_RESPO_FK
     foreign key (usernameResponsabile)
     references USR_RESPONSABILE(username);

alter table INTERVENTO add constraint REF_INTER_TECNI_FK
     foreign key (usernameTecnico)
     references USR_TECNICO(username);

alter table INTERVENTO add constraint REF_INTER_TIPO__FK
     foreign key (tipo)
     references INT_TIPO(tipo);

alter table MACC_BIOGAS add constraint SID_MACC__INSTA_2_FK
     foreign key (codiceInstallazione)
     references MACCHINARIO(codiceInstallazione);

alter table MACC_BIOGAS add constraint REF_MACC__IMPIA_2
     foreign key (codiceImpianto, siglaProvincia)
     references IMPIANTO(codiceImpianto, siglaProvincia);

/*MySql does not support select clauses inside check() */
/*
alter table MACC_EOLICO add constraint ID_MACC_EOLICO_CHK
     check(exists(select * from MACC_EOLICO_SPECIFICHE
                  where MACC_EOLICO_SPECIFICHE.siglaProvincia = siglaProvincia and MACC_EOLICO_SPECIFICHE.codiceImpianto = codiceImpianto and MACC_EOLICO_SPECIFICHE.codice = codice));
*/

alter table MACC_EOLICO add constraint SID_MACC__INSTA_1_FK
     foreign key (codiceInstallazione)
     references MACCHINARIO(codiceInstallazione);

alter table MACC_EOLICO add constraint REF_MACC__IMPIA_1
     foreign key (codiceImpianto, siglaProvincia)
     references IMPIANTO(codiceImpianto, siglaProvincia);

alter table MACC_FOTOVOLTAICO add constraint SID_MACC__INSTA_FK
     foreign key (codiceInstallazione)
     references MACCHINARIO(codiceInstallazione);

alter table MACC_FOTOVOLTAICO add constraint REF_MACC__IMPIA
     foreign key (codiceImpianto, siglaProvincia)
     references IMPIANTO(codiceImpianto, siglaProvincia);

/*MySql does not support select clauses inside check() */
/*
alter table MODELLO add constraint ID_MODELLO_CHK
     check(exists(select * from COMP_MODELLO
                  where COMP_MODELLO.aziendaModello = azienda and COMP_MODELLO.nomeModello = nome));
*/

alter table MONITORAGGIO add constraint EQU_MONIT_ADDET
     foreign key (usernameAddetto)
     references USR_ADDETTO(username);

alter table MONITORAGGIO add constraint REF_MONIT_IMPIA_FK
     foreign key (codiceImpianto, siglaProvincia)
     references IMPIANTO(codiceImpianto, siglaProvincia);

alter table MACC_PRODUZIONE add constraint REF_PRODU_INSTA
     foreign key (codiceInstallazione)
     references MACCHINARIO(codiceInstallazione);

alter table IMP_RILEVAZIONE add constraint REF_RILEV_IMPIA
     foreign key (codiceImpianto, siglaProvincia)
     references IMPIANTO(codiceImpianto, siglaProvincia);

alter table MACC_EOLICO_SPECIFICHE add constraint EQU_SPEC__MACC_
     foreign key (codiceImpianto, siglaProvincia, codiceInterno)
     references MACC_EOLICO(codiceImpianto, siglaProvincia, codiceInterno);

alter table USR_TECNICO add constraint REF_TECNI_PROVI_FK
     foreign key (siglaProvincia)
     references PROVINCIA(sigla);

