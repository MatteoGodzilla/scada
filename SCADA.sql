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

create database SCHEMA;


-- DBSpace Section
-- _______________


-- Tables Section
-- _____________ 

create table ADDETTO (
     username varchar(32) not null,
     password varchar(32) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     regione varchar(21) not null,
     constraint ID_ADDETTO_ID primary key (username));

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
     msrp numeric(69) not null,
     constraint ID_COMPONENTE_ID primary key (azienda, codice));

comment on column COMPONENTE.msrp is ' #user-defined="INT" ';

create table GARANZIA (
     azienda varchar(64) not null,
     nomeModello varchar(32) not null,
     durataAnni numeric(69) not null,
     costo numeric(69) not null,
     descrizione varchar(1024) not null,
     constraint ID_GARANZIA_ID primary key (azienda, nomeModello, durataAnni));

comment on column GARANZIA.durataAnni is ' #user-defined="INT" ';
comment on column GARANZIA.costo is ' #user-defined="INT" ';

create table IMPIANTO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     indirizzo varchar(128) not null,
     area float(1) not null,
     uomoInSito char not null,
     tipologia numeric(69) not null,
     constraint ID_IMPIANTO_ID primary key (siglaProvincia, codiceImpianto));

comment on column IMPIANTO.codiceImpianto is ' #user-defined="INT" ';
comment on column IMPIANTO.tipologia is ' #user-defined="INT" ';

create table INST_STATUS (
     tipo numeric(69) not null,
     descrizione varchar(32) not null,
     constraint ID_INST_STATUS_ID primary key (tipo));

comment on column INST_STATUS.tipo is ' #user-defined="INT" ';

create table INSTALLAZIONE (
     codiceInstallazione numeric(69) not null,
     dataInstallazione date not null,
     tipologia numeric(69) not null,
     azienda varchar(64) not null,
     nomeModello varchar(32) not null,
     durataGaranzia numeric(69) not null,
     status numeric(69) not null,
     constraint ID_INSTALLAZIONE_ID primary key (codiceInstallazione));

comment on column INSTALLAZIONE.codiceInstallazione is ' #user-defined="INT" ';
comment on column INSTALLAZIONE.tipologia is ' #user-defined="INT" ';
comment on column INSTALLAZIONE.durataGaranzia is ' #user-defined="INT" ';
comment on column INSTALLAZIONE.status is ' #user-defined="INT" ';

create table INT_IMPIANTO (
     codiceIntervento numeric(69) not null,
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     constraint ID_INT_IMPIANTO_ID primary key (codiceIntervento, siglaProvincia, codiceImpianto));

comment on column INT_IMPIANTO.codiceIntervento is ' #user-defined="INT" ';
comment on column INT_IMPIANTO.codiceImpianto is ' #user-defined="INT" ';

create table INT_MACHCINARIO (
     codiceInstallazione numeric(69) not null,
     codiceIntervento numeric(69) not null,
     constraint ID_INT_MACHCINARIO_ID primary key (codiceIntervento, codiceInstallazione));

comment on column INT_MACHCINARIO.codiceInstallazione is ' #user-defined="INT" ';
comment on column INT_MACHCINARIO.codiceIntervento is ' #user-defined="INT" ';

create table INTERVENTO (
     codice numeric(69) not null,
     note varchar(1024),
     usernameResponsabile varchar(32) not null,
     usernameTecnico varchar(32),
     tipo numeric(69) not null,
     constraint ID_INTERVENTO_ID primary key (codice));

comment on column INTERVENTO.codice is ' #user-defined="INT" ';
comment on column INTERVENTO.tipo is ' #user-defined="INT" ';

create table MACC_BIOGAS (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     codice numeric(69) not null,
     codiceInstallazione numeric(69) not null,
     kwhOttimo float(1) not null,
     kgBatteri float(1) not null,
     kgUmido float(1) not null,
     constraint ID_MACC_BIOGAS_ID primary key (siglaProvincia, codiceImpianto, codice),
     constraint SID_MACC__INSTA_2_ID unique (codiceInstallazione));

comment on column MACC_BIOGAS.codiceImpianto is ' #user-defined="INT" ';
comment on column MACC_BIOGAS.codice is ' #user-defined="INT" ';
comment on column MACC_BIOGAS.codiceInstallazione is ' #user-defined="INT" ';

create table MACC_EOLICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     codice numeric(69) not null,
     codiceInstallazione numeric(69) not null,
     constraint ID_MACC_EOLICO_ID primary key (siglaProvincia, codiceImpianto, codice),
     constraint SID_MACC__INSTA_1_ID unique (codiceInstallazione));

comment on column MACC_EOLICO.codiceImpianto is ' #user-defined="INT" ';
comment on column MACC_EOLICO.codice is ' #user-defined="INT" ';
comment on column MACC_EOLICO.codiceInstallazione is ' #user-defined="INT" ';

create table MACC_FOTOVOLTAICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     codice numeric(69) not null,
     codiceInstallazione numeric(69) not null,
     celle numeric(69) not null,
     kwhMax float(1) not null,
     angolo float(1) not null,
     constraint ID_MACC_FOTOVOLTAICO_ID primary key (siglaProvincia, codiceImpianto, codice),
     constraint SID_MACC__INSTA_ID unique (codiceInstallazione));

comment on column MACC_FOTOVOLTAICO.codiceImpianto is ' #user-defined="INT" ';
comment on column MACC_FOTOVOLTAICO.codice is ' #user-defined="INT" ';
comment on column MACC_FOTOVOLTAICO.codiceInstallazione is ' #user-defined="INT" ';
comment on column MACC_FOTOVOLTAICO.celle is ' #user-defined="INT" ';

create table MODELLO (
     azienda varchar(64) not null,
     nome varchar(32) not null,
     area float(1) not null,
     constraint ID_MODELLO_ID primary key (azienda, nome));

create table MONITORAGGIO (
     siglaProvincia char(2) not null,
     codice numeric(69) not null,
     usernameAddetto varchar(32) not null,
     constraint ID_MONITORAGGIO_ID primary key (usernameAddetto, siglaProvincia, codice));

comment on column MONITORAGGIO.codice is ' #user-defined="INT" ';

create table PRODUZIONE (
     codiceInstallazione numeric(69) not null,
     timestamp date not null,
     kwh float(1) not null,
     constraint ID_PRODUZIONE_ID primary key (codiceInstallazione, timestamp));

comment on column PRODUZIONE.codiceInstallazione is ' #user-defined="INT" ';
comment on column PRODUZIONE.timestamp is ' #user-defined="TIMESTAMP" ';

create table PROVINCIA (
     sigla char(2) not null,
     regione varchar(21) not null,
     constraint ID_PROVINCIA_ID primary key (sigla));

create table RESPONSABILE (
     username varchar(32) not null,
     password varchar(32) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     regione varchar(21) not null,
     constraint ID_RESPONSABILE_ID primary key (username));

create table RILEVAZIONE (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     data date not null,
     vento float(1) not null,
     uv float(1) not null,
     constraint ID_RILEVAZIONE_ID primary key (siglaProvincia, codiceImpianto, data));

comment on column RILEVAZIONE.codiceImpianto is ' #user-defined="INT" ';
comment on column RILEVAZIONE.data is ' #user-defined="TIMESTAMP" ';

create table SPEC_EOLICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(69) not null,
     codice numeric(69) not null,
     nodi float(1) not null,
     kwh float(1) not null,
     constraint ID_SPEC_EOLICO_ID primary key (siglaProvincia, codiceImpianto, codice, nodi));

comment on column SPEC_EOLICO.codiceImpianto is ' #user-defined="INT" ';
comment on column SPEC_EOLICO.codice is ' #user-defined="INT" ';

create table TECNICO (
     username varchar(32) not null,
     password varchar(32) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     siglaProvincia char(2) not null,
     constraint ID_TECNICO_ID primary key (username));

create table TIPO_INTERVENTO (
     tipo numeric(69) not null,
     descrizione varchar(32) not null,
     constraint ID_TIPO_INTERVENTO_ID primary key (tipo));

comment on column TIPO_INTERVENTO.tipo is ' #user-defined="INT" ';

create table TIPOLOGIA (
     codice numeric(69) not null,
     Descrizione varchar(32) not null,
     constraint ID_TIPOLOGIA_ID primary key (codice));

comment on column TIPOLOGIA.codice is ' #user-defined="INT" ';


-- Constraints Section
-- ___________________ 

alter table ADDETTO add constraint ID_ADDETTO_CHK
     check(exists(select * from MONITORAGGIO
                  where MONITORAGGIO.usernameAddetto = username)); 

alter table COMP_MODELLO add constraint EQU_COMP__MODEL
     foreign key (aziendaModello, nomeModello)
     references MODELLO;

alter table COMP_MODELLO add constraint REF_COMP__COMPO_FK
     foreign key (aziendaComponente, codiceComponente)
     references COMPONENTE;

alter table GARANZIA add constraint REF_GARAN_MODEL
     foreign key (azienda, nomeModello)
     references MODELLO;

alter table IMPIANTO add constraint REF_IMPIA_PROVI
     foreign key (siglaProvincia)
     references PROVINCIA;

alter table IMPIANTO add constraint REF_IMPIA_TIPOL_FK
     foreign key (tipologia)
     references TIPOLOGIA;

alter table INSTALLAZIONE add constraint REF_INSTA_TIPOL_FK
     foreign key (tipologia)
     references TIPOLOGIA;

alter table INSTALLAZIONE add constraint REF_INSTA_MODEL_FK
     foreign key (azienda, nomeModello)
     references MODELLO;

alter table INSTALLAZIONE add constraint REF_INSTA_INST__FK
     foreign key (status)
     references INST_STATUS;

alter table INT_IMPIANTO add constraint REF_INT_I_IMPIA_FK
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table INT_IMPIANTO add constraint REF_INT_I_INTER
     foreign key (codiceIntervento)
     references INTERVENTO;

alter table INT_MACHCINARIO add constraint REF_INT_M_INTER
     foreign key (codiceIntervento)
     references INTERVENTO;

alter table INT_MACHCINARIO add constraint REF_INT_M_INSTA_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table INTERVENTO add constraint REF_INTER_RESPO_FK
     foreign key (usernameResponsabile)
     references RESPONSABILE;

alter table INTERVENTO add constraint REF_INTER_TECNI_FK
     foreign key (usernameTecnico)
     references TECNICO;

alter table INTERVENTO add constraint REF_INTER_TIPO__FK
     foreign key (tipo)
     references TIPO_INTERVENTO;

alter table MACC_BIOGAS add constraint SID_MACC__INSTA_2_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table MACC_BIOGAS add constraint REF_MACC__IMPIA_2
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table MACC_EOLICO add constraint ID_MACC_EOLICO_CHK
     check(exists(select * from SPEC_EOLICO
                  where SPEC_EOLICO.siglaProvincia = siglaProvincia and SPEC_EOLICO.codiceImpianto = codiceImpianto and SPEC_EOLICO.codice = codice)); 

alter table MACC_EOLICO add constraint SID_MACC__INSTA_1_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table MACC_EOLICO add constraint REF_MACC__IMPIA_1
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table MACC_FOTOVOLTAICO add constraint SID_MACC__INSTA_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table MACC_FOTOVOLTAICO add constraint REF_MACC__IMPIA
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table MODELLO add constraint ID_MODELLO_CHK
     check(exists(select * from COMP_MODELLO
                  where COMP_MODELLO.aziendaModello = azienda and COMP_MODELLO.nomeModello = nome)); 

alter table MONITORAGGIO add constraint EQU_MONIT_ADDET
     foreign key (usernameAddetto)
     references ADDETTO;

alter table MONITORAGGIO add constraint REF_MONIT_IMPIA_FK
     foreign key (siglaProvincia, codice)
     references IMPIANTO;

alter table PRODUZIONE add constraint REF_PRODU_INSTA
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table RILEVAZIONE add constraint REF_RILEV_IMPIA
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table SPEC_EOLICO add constraint EQU_SPEC__MACC_
     foreign key (siglaProvincia, codiceImpianto, codice)
     references MACC_EOLICO;

alter table TECNICO add constraint REF_TECNI_PROVI_FK
     foreign key (siglaProvincia)
     references PROVINCIA;


-- Index Section
-- _____________ 

create unique index ID_ADDETTO_IND
     on ADDETTO (username);

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

create unique index ID_INST_STATUS_IND
     on INST_STATUS (tipo);

create unique index ID_INSTALLAZIONE_IND
     on INSTALLAZIONE (codiceInstallazione);

create index REF_INSTA_TIPOL_IND
     on INSTALLAZIONE (tipologia);

create index REF_INSTA_MODEL_IND
     on INSTALLAZIONE (azienda, nomeModello);

create index REF_INSTA_INST__IND
     on INSTALLAZIONE (status);

create unique index ID_INT_IMPIANTO_IND
     on INT_IMPIANTO (codiceIntervento, siglaProvincia, codiceImpianto);

create index REF_INT_I_IMPIA_IND
     on INT_IMPIANTO (siglaProvincia, codiceImpianto);

create unique index ID_INT_MACHCINARIO_IND
     on INT_MACHCINARIO (codiceIntervento, codiceInstallazione);

create index REF_INT_M_INSTA_IND
     on INT_MACHCINARIO (codiceInstallazione);

create unique index ID_INTERVENTO_IND
     on INTERVENTO (codice);

create index REF_INTER_RESPO_IND
     on INTERVENTO (usernameResponsabile);

create index REF_INTER_TECNI_IND
     on INTERVENTO (usernameTecnico);

create index REF_INTER_TIPO__IND
     on INTERVENTO (tipo);

create unique index ID_MACC_BIOGAS_IND
     on MACC_BIOGAS (siglaProvincia, codiceImpianto, codice);

create unique index SID_MACC__INSTA_2_IND
     on MACC_BIOGAS (codiceInstallazione);

create unique index ID_MACC_EOLICO_IND
     on MACC_EOLICO (siglaProvincia, codiceImpianto, codice);

create unique index SID_MACC__INSTA_1_IND
     on MACC_EOLICO (codiceInstallazione);

create unique index ID_MACC_FOTOVOLTAICO_IND
     on MACC_FOTOVOLTAICO (siglaProvincia, codiceImpianto, codice);

create unique index SID_MACC__INSTA_IND
     on MACC_FOTOVOLTAICO (codiceInstallazione);

create unique index ID_MODELLO_IND
     on MODELLO (azienda, nome);

create unique index ID_MONITORAGGIO_IND
     on MONITORAGGIO (usernameAddetto, siglaProvincia, codice);

create index REF_MONIT_IMPIA_IND
     on MONITORAGGIO (siglaProvincia, codice);

create unique index ID_PRODUZIONE_IND
     on PRODUZIONE (codiceInstallazione, timestamp);

create unique index ID_PROVINCIA_IND
     on PROVINCIA (sigla);

create unique index ID_RESPONSABILE_IND
     on RESPONSABILE (username);

create unique index ID_RILEVAZIONE_IND
     on RILEVAZIONE (siglaProvincia, codiceImpianto, data);

create unique index ID_SPEC_EOLICO_IND
     on SPEC_EOLICO (siglaProvincia, codiceImpianto, codice, nodi);

create unique index ID_TECNICO_IND
     on TECNICO (username);

create index REF_TECNI_PROVI_IND
     on TECNICO (siglaProvincia);

create unique index ID_TIPO_INTERVENTO_IND
     on TIPO_INTERVENTO (tipo);

create unique index ID_TIPOLOGIA_IND
     on TIPOLOGIA (codice);

