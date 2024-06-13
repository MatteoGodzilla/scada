-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Thu Jun 13 19:53:19 2024 
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

create table BIOGAS (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     constraint ID_BIOGA_IMPIA_ID primary key (siglaProvincia, codiceImpianto));

comment on column BIOGAS.codiceImpianto is ' #user-defined="INT" ';

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
     msrp numeric(1) not null,
     constraint ID_COMPONENTE_ID primary key (azienda, codice));

comment on column COMPONENTE.msrp is ' #user-defined="INT" ';

create table EOLICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     constraint ID_EOLIC_IMPIA_ID primary key (siglaProvincia, codiceImpianto));

comment on column EOLICO.codiceImpianto is ' #user-defined="INT" ';

create table FOTOVOLTAICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     constraint ID_FOTOV_IMPIA_ID primary key (siglaProvincia, codiceImpianto));

comment on column FOTOVOLTAICO.codiceImpianto is ' #user-defined="INT" ';

create table GARANZIA (
     azienda varchar(64) not null,
     nome varchar(32) not null,
     scadenza date not null,
     costo numeric(1) not null,
     descrizione varchar(1024) not null,
     constraint ID_GARANZIA_ID primary key (azienda, nome, scadenza));

comment on column GARANZIA.costo is ' #user-defined="INT" ';

create table IMPIANTO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     indirizzo varchar(128) not null,
     area float(1) not null,
     uomoInSito char not null,
     EOLICO char(1),
     FOTOVOLTAICO char(1),
     BIOGAS char(1),
     constraint ID_IMPIANTO_ID primary key (siglaProvincia, codiceImpianto));

comment on column IMPIANTO.codiceImpianto is ' #user-defined="INT" ';

create table INST_BIOGAS (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     codice numeric(1) not null,
     codiceInstallazione numeric(1) not null,
     kwhOttimo float(1) not null,
     kgBatteri float(1) not null,
     kgUmido float(1) not null,
     constraint ID_INST_BIOGAS_ID primary key (siglaProvincia, codiceImpianto, codice),
     constraint SID_INST__INSTA_2_ID unique (codiceInstallazione));

comment on column INST_BIOGAS.codiceImpianto is ' #user-defined="INT" ';
comment on column INST_BIOGAS.codice is ' #user-defined="INT" ';
comment on column INST_BIOGAS.codiceInstallazione is ' #user-defined="INT" ';

create table INST_EOLICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     codice numeric(1) not null,
     codiceInstallazione numeric(1) not null,
     constraint ID_INST_EOLICO_ID primary key (siglaProvincia, codiceImpianto, codice),
     constraint SID_INST__INSTA_1_ID unique (codiceInstallazione));

comment on column INST_EOLICO.codiceImpianto is ' #user-defined="INT" ';
comment on column INST_EOLICO.codice is ' #user-defined="INT" ';
comment on column INST_EOLICO.codiceInstallazione is ' #user-defined="INT" ';

create table INST_FOTOVOLTAICO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     codice numeric(1) not null,
     codiceInstallazione numeric(1) not null,
     celle numeric(1) not null,
     kwhMax float(1) not null,
     angolo float(1) not null,
     constraint ID_INST_FOTOVOLTAICO_ID primary key (siglaProvincia, codiceImpianto, codice),
     constraint SID_INST__INSTA_ID unique (codiceInstallazione));

comment on column INST_FOTOVOLTAICO.codiceImpianto is ' #user-defined="INT" ';
comment on column INST_FOTOVOLTAICO.codice is ' #user-defined="INT" ';
comment on column INST_FOTOVOLTAICO.codiceInstallazione is ' #user-defined="INT" ';
comment on column INST_FOTOVOLTAICO.celle is ' #user-defined="INT" ';

create table INSTALLAZIONE (
     codiceInstallazione numeric(1) not null,
     status char(1) not null,
     dataInstallazione date not null,
     codiceContratto numeric(1) not null,
     MACC_EOLICO char(1),
     MACC_FOTOVOLTAICO char(1),
     MACC_BIOGAS char(1),
     aziendaModello varchar(64) not null,
     nomeModello varchar(32) not null,
     constraint ID_INSTALLAZIONE_ID primary key (codiceInstallazione));

comment on column INSTALLAZIONE.codiceInstallazione is ' #user-defined="INT" ';
comment on column INSTALLAZIONE.codiceContratto is ' #user-defined="INT" ';

create table INTERVENTO (
     codice numeric(1) not null,
     tipologia varchar(32) not null,
     note varchar(1024),
     usernameResponsabile varchar(32) not null,
     usernameTecnico varchar(32),
     constraint ID_INTERVENTO_ID primary key (codice));

comment on column INTERVENTO.codice is ' #user-defined="INT" ';

create table INTERVENTO_IMPIANTO (
     codiceIntervento numeric(1) not null,
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     constraint ID_INTERVENTO_IMPIANTO_ID primary key (codiceIntervento, siglaProvincia, codiceImpianto));

comment on column INTERVENTO_IMPIANTO.codiceIntervento is ' #user-defined="INT" ';
comment on column INTERVENTO_IMPIANTO.codiceImpianto is ' #user-defined="INT" ';

create table INTERVENTO_MACCHINARIO (
     codiceInstallazione numeric(1) not null,
     codiceIntervento numeric(1) not null,
     constraint ID_INTERVENTO_MACCHINARIO_ID primary key (codiceIntervento, codiceInstallazione));

comment on column INTERVENTO_MACCHINARIO.codiceInstallazione is ' #user-defined="INT" ';
comment on column INTERVENTO_MACCHINARIO.codiceIntervento is ' #user-defined="INT" ';

create table MODELLO (
     azienda varchar(64) not null,
     nome varchar(32) not null,
     area float(1) not null,
     constraint ID_MODELLO_ID primary key (azienda, nome));

create table MONITORAGGIO (
     siglaProvincia char(2) not null,
     codice numeric(1) not null,
     usernameAddetto varchar(32) not null,
     constraint ID_MONITORAGGIO_ID primary key (usernameAddetto, siglaProvincia, codice));

comment on column MONITORAGGIO.codice is ' #user-defined="INT" ';

create table PRODUZIONE (
     codiceInstallazione numeric(1) not null,
     data date not null,
     kwh float(1) not null,
     constraint ID_PRODUZIONE_ID primary key (codiceInstallazione, data));

comment on column PRODUZIONE.codiceInstallazione is ' #user-defined="INT" ';
comment on column PRODUZIONE.data is ' #user-defined="TIMESTAMP" ';

create table PROVINCIA (
     sigla char(2) not null,
     regione varchar(21) not null,
     constraint ID_PROVINCIA_ID primary key (sigla));

create table RESPONSABILE (
     username varchar(32) not null,
     password varchar(32) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     regione char(21) not null,
     constraint ID_RESPONSABILE_ID primary key (username));

create table RILEVAZIONE_UV (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     timestamp date not null,
     uv float(1) not null,
     constraint ID_RILEVAZIONE_UV_ID primary key (siglaProvincia, codiceImpianto, timestamp));

comment on column RILEVAZIONE_UV.codiceImpianto is ' #user-defined="INT" ';
comment on column RILEVAZIONE_UV.timestamp is ' #user-defined="TIMESTAMP" ';

create table RILEVAZIONE_VENTO (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     timestamp date not null,
     vento float(1) not null,
     constraint ID_RILEVAZIONE_VENTO_ID primary key (siglaProvincia, codiceImpianto, timestamp));

comment on column RILEVAZIONE_VENTO.codiceImpianto is ' #user-defined="INT" ';
comment on column RILEVAZIONE_VENTO.timestamp is ' #user-defined="TIMESTAMP" ';

create table SPECIFICHE (
     siglaProvincia char(2) not null,
     codiceImpianto numeric(1) not null,
     codice numeric(1) not null,
     nodi float(1) not null,
     kwh float(1) not null,
     constraint ID_SPECIFICHE_ID primary key (siglaProvincia, codiceImpianto, codice, nodi, kwh));

comment on column SPECIFICHE.codiceImpianto is ' #user-defined="INT" ';
comment on column SPECIFICHE.codice is ' #user-defined="INT" ';

create table TECNICO (
     username varchar(32) not null,
     password varchar(32) not null,
     nome varchar(64) not null,
     cognome varchar(64) not null,
     siglaProvincia char(2) not null,
     constraint ID_TECNICO_ID primary key (username));


-- Constraints Section
-- ___________________ 

alter table ADDETTO add constraint ID_ADDETTO_CHK
     check(exists(select * from MONITORAGGIO
                  where MONITORAGGIO.usernameAddetto = username)); 

alter table BIOGAS add constraint ID_BIOGA_IMPIA_FK
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table COMP_MODELLO add constraint EQU_COMP__MODEL
     foreign key (aziendaModello, nomeModello)
     references MODELLO;

alter table COMP_MODELLO add constraint REF_COMP__COMPO_FK
     foreign key (aziendaComponente, codiceComponente)
     references COMPONENTE;

alter table EOLICO add constraint ID_EOLIC_IMPIA_FK
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table FOTOVOLTAICO add constraint ID_FOTOV_IMPIA_FK
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table GARANZIA add constraint REF_GARAN_MODEL
     foreign key (azienda, nome)
     references MODELLO;

alter table IMPIANTO add constraint REF_IMPIA_PROVI
     foreign key (siglaProvincia)
     references PROVINCIA;

alter table IMPIANTO add constraint EXTONE_IMPIANTO
     check((BIOGAS is not null and FOTOVOLTAICO is null and EOLICO is null)
           or (BIOGAS is null and FOTOVOLTAICO is not null and EOLICO is null)
           or (BIOGAS is null and FOTOVOLTAICO is null and EOLICO is not null)); 

alter table INST_BIOGAS add constraint SID_INST__INSTA_2_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table INST_BIOGAS add constraint REF_INST__BIOGA
     foreign key (siglaProvincia, codiceImpianto)
     references BIOGAS;

alter table INST_EOLICO add constraint ID_INST_EOLICO_CHK
     check(exists(select * from SPECIFICHE
                  where SPECIFICHE.siglaProvincia = siglaProvincia and SPECIFICHE.codiceImpianto = codiceImpianto and SPECIFICHE.codice = codice)); 

alter table INST_EOLICO add constraint SID_INST__INSTA_1_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table INST_EOLICO add constraint REF_INST__EOLIC
     foreign key (siglaProvincia, codiceImpianto)
     references EOLICO;

alter table INST_FOTOVOLTAICO add constraint SID_INST__INSTA_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

alter table INST_FOTOVOLTAICO add constraint REF_INST__FOTOV
     foreign key (siglaProvincia, codiceImpianto)
     references FOTOVOLTAICO;

alter table INSTALLAZIONE add constraint EXTONE_INSTALLAZIONE
     check((MACC_BIOGAS is not null and MACC_FOTOVOLTAICO is null and MACC_EOLICO is null)
           or (MACC_BIOGAS is null and MACC_FOTOVOLTAICO is not null and MACC_EOLICO is null)
           or (MACC_BIOGAS is null and MACC_FOTOVOLTAICO is null and MACC_EOLICO is not null)); 

alter table INSTALLAZIONE add constraint REF_INSTA_MODEL_FK
     foreign key (aziendaModello, nomeModello)
     references MODELLO;

alter table INTERVENTO add constraint REF_INTER_RESPO_FK
     foreign key (usernameResponsabile)
     references RESPONSABILE;

alter table INTERVENTO add constraint REF_INTER_TECNI_FK
     foreign key (usernameTecnico)
     references TECNICO;

alter table INTERVENTO_IMPIANTO add constraint REF_INTER_IMPIA_FK
     foreign key (siglaProvincia, codiceImpianto)
     references IMPIANTO;

alter table INTERVENTO_IMPIANTO add constraint REF_INTER_INTER_1
     foreign key (codiceIntervento)
     references INTERVENTO;

alter table INTERVENTO_MACCHINARIO add constraint REF_INTER_INTER
     foreign key (codiceIntervento)
     references INTERVENTO;

alter table INTERVENTO_MACCHINARIO add constraint REF_INTER_INSTA_FK
     foreign key (codiceInstallazione)
     references INSTALLAZIONE;

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

alter table RILEVAZIONE_UV add constraint REF_RILEV_FOTOV
     foreign key (siglaProvincia, codiceImpianto)
     references FOTOVOLTAICO;

alter table RILEVAZIONE_VENTO add constraint REF_RILEV_EOLIC
     foreign key (siglaProvincia, codiceImpianto)
     references EOLICO;

alter table SPECIFICHE add constraint EQU_SPECI_INST_
     foreign key (siglaProvincia, codiceImpianto, codice)
     references INST_EOLICO;

alter table TECNICO add constraint REF_TECNI_PROVI_FK
     foreign key (siglaProvincia)
     references PROVINCIA;


-- Index Section
-- _____________ 

create unique index ID_ADDETTO_IND
     on ADDETTO (username);

create unique index ID_BIOGA_IMPIA_IND
     on BIOGAS (siglaProvincia, codiceImpianto);

create unique index ID_COMP_MODELLO_IND
     on COMP_MODELLO (aziendaModello, nomeModello, aziendaComponente, codiceComponente);

create index REF_COMP__COMPO_IND
     on COMP_MODELLO (aziendaComponente, codiceComponente);

create unique index ID_COMPONENTE_IND
     on COMPONENTE (azienda, codice);

create unique index ID_EOLIC_IMPIA_IND
     on EOLICO (siglaProvincia, codiceImpianto);

create unique index ID_FOTOV_IMPIA_IND
     on FOTOVOLTAICO (siglaProvincia, codiceImpianto);

create unique index ID_GARANZIA_IND
     on GARANZIA (azienda, nome, scadenza);

create unique index ID_IMPIANTO_IND
     on IMPIANTO (siglaProvincia, codiceImpianto);

create unique index ID_INST_BIOGAS_IND
     on INST_BIOGAS (siglaProvincia, codiceImpianto, codice);

create unique index SID_INST__INSTA_2_IND
     on INST_BIOGAS (codiceInstallazione);

create unique index ID_INST_EOLICO_IND
     on INST_EOLICO (siglaProvincia, codiceImpianto, codice);

create unique index SID_INST__INSTA_1_IND
     on INST_EOLICO (codiceInstallazione);

create unique index ID_INST_FOTOVOLTAICO_IND
     on INST_FOTOVOLTAICO (siglaProvincia, codiceImpianto, codice);

create unique index SID_INST__INSTA_IND
     on INST_FOTOVOLTAICO (codiceInstallazione);

create unique index ID_INSTALLAZIONE_IND
     on INSTALLAZIONE (codiceInstallazione);

create index REF_INSTA_MODEL_IND
     on INSTALLAZIONE (aziendaModello, nomeModello);

create unique index ID_INTERVENTO_IND
     on INTERVENTO (codice);

create index REF_INTER_RESPO_IND
     on INTERVENTO (usernameResponsabile);

create index REF_INTER_TECNI_IND
     on INTERVENTO (usernameTecnico);

create unique index ID_INTERVENTO_IMPIANTO_IND
     on INTERVENTO_IMPIANTO (codiceIntervento, siglaProvincia, codiceImpianto);

create index REF_INTER_IMPIA_IND
     on INTERVENTO_IMPIANTO (siglaProvincia, codiceImpianto);

create unique index ID_INTERVENTO_MACCHINARIO_IND
     on INTERVENTO_MACCHINARIO (codiceIntervento, codiceInstallazione);

create index REF_INTER_INSTA_IND
     on INTERVENTO_MACCHINARIO (codiceInstallazione);

create unique index ID_MODELLO_IND
     on MODELLO (azienda, nome);

create unique index ID_MONITORAGGIO_IND
     on MONITORAGGIO (usernameAddetto, siglaProvincia, codice);

create index REF_MONIT_IMPIA_IND
     on MONITORAGGIO (siglaProvincia, codice);

create unique index ID_PRODUZIONE_IND
     on PRODUZIONE (codiceInstallazione, data);

create unique index ID_PROVINCIA_IND
     on PROVINCIA (sigla);

create unique index ID_RESPONSABILE_IND
     on RESPONSABILE (username);

create unique index ID_RILEVAZIONE_UV_IND
     on RILEVAZIONE_UV (siglaProvincia, codiceImpianto, timestamp);

create unique index ID_RILEVAZIONE_VENTO_IND
     on RILEVAZIONE_VENTO (siglaProvincia, codiceImpianto, timestamp);

create unique index ID_SPECIFICHE_IND
     on SPECIFICHE (siglaProvincia, codiceImpianto, codice, nodi, kwh);

create unique index ID_TECNICO_IND
     on TECNICO (username);

create index REF_TECNI_PROVI_IND
     on TECNICO (siglaProvincia);

