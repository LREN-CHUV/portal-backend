--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 9.5.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: app; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE app (
    id integer NOT NULL,
    author character varying(255),
    category character varying(255),
    description text,
    email character varying(255),
    image text,
    link character varying(255),
    name character varying(255),
    ratingcount integer,
    totalrating integer
);


ALTER TABLE app OWNER TO postgres;

--
-- Name: article; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE article (
    slug character varying(255) NOT NULL,
    abstract text,
    content text,
    createdat timestamp without time zone,
    publishedat timestamp without time zone,
    status character varying(255),
    title character varying(255) NOT NULL,
    updatedat timestamp without time zone,
    createdby_username character varying(255),
    updatedby_username character varying(255)
);


ALTER TABLE article OWNER TO postgres;

--
-- Name: article_tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE article_tag (
    article_slug character varying(255) NOT NULL,
    tags_name character varying(255) NOT NULL
);


ALTER TABLE article_tag OWNER TO postgres;

--
-- Name: config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE config (
    id bigint NOT NULL,
    hasxaxis boolean,
    height integer,
    type character varying(255),
    xaxisvariable character varying(255)
);


ALTER TABLE config OWNER TO postgres;

--
-- Name: config_title; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE config_title (
    config_id bigint NOT NULL,
    title character varying(255),
    title_key character varying(255) NOT NULL
);


ALTER TABLE config_title OWNER TO postgres;

--
-- Name: config_yaxisvariables; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE config_yaxisvariables (
    config_id bigint NOT NULL,
    yaxisvariables character varying(255)
);


ALTER TABLE config_yaxisvariables OWNER TO postgres;

--
-- Name: dataset; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE dataset (
    code character varying(255) NOT NULL,
    date timestamp without time zone
);


ALTER TABLE dataset OWNER TO postgres;

--
-- Name: dataset_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE dataset_data (
    dataset_code character varying(255) NOT NULL,
    data bytea,
    data_key character varying(255) NOT NULL
);


ALTER TABLE dataset_data OWNER TO postgres;

--
-- Name: dataset_grouping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE dataset_grouping (
    dataset_code character varying(255) NOT NULL,
    "grouping" character varying(255)
);


ALTER TABLE dataset_grouping OWNER TO postgres;

--
-- Name: dataset_header; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE dataset_header (
    dataset_code character varying(255) NOT NULL,
    header character varying(255)
);


ALTER TABLE dataset_header OWNER TO postgres;

--
-- Name: dataset_variable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE dataset_variable (
    dataset_code character varying(255) NOT NULL,
    variable character varying(255)
);


ALTER TABLE dataset_variable OWNER TO postgres;

--
-- Name: experiment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE experiment (
    uuid uuid NOT NULL,
    algorithms text,
    created timestamp without time zone,
    finished timestamp without time zone,
    haserror boolean NOT NULL,
    hasservererror boolean NOT NULL,
    name text,
    result text,
    resultsviewed boolean NOT NULL,
    shared boolean NOT NULL,
    validations text,
    createdby_username character varying(255),
    model_slug character varying(255)
);


ALTER TABLE experiment OWNER TO postgres;

--
-- Name: filter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE filter (
    id bigint NOT NULL,
    operator character varying(255),
    variable_code character varying(255)
);


ALTER TABLE filter OWNER TO postgres;

--
-- Name: filter_values; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE filter_values (
    filter_id bigint NOT NULL,
    "values" character varying(255)
);


ALTER TABLE filter_values OWNER TO postgres;

--
-- Name: group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "group" (
    code character varying(255) NOT NULL,
    label character varying(255),
    parent_code character varying(255)
);


ALTER TABLE "group" OWNER TO postgres;

--
-- Name: group_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE group_group (
    group_code character varying(255) NOT NULL,
    groups_code character varying(255) NOT NULL
);


ALTER TABLE group_group OWNER TO postgres;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

--
-- Name: model; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE model (
    slug character varying(255) NOT NULL,
    createdat timestamp without time zone,
    description character varying(255),
    textquery text,
    title character varying(255),
    updatedat timestamp without time zone,
    valid boolean,
    config_id bigint,
    createdby_username character varying(255),
    dataset_code character varying(255),
    query_id bigint,
    updatedby_username character varying(255)
);


ALTER TABLE model OWNER TO postgres;

--
-- Name: query; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE query (
    id bigint NOT NULL,
    request character varying(255)
);


ALTER TABLE query OWNER TO postgres;

--
-- Name: query_covariable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE query_covariable (
    id bigint NOT NULL,
    code character varying(255) NOT NULL
);


ALTER TABLE query_covariable OWNER TO postgres;

--
-- Name: query_filter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE query_filter (
    query_id bigint NOT NULL,
    filters_id bigint NOT NULL
);


ALTER TABLE query_filter OWNER TO postgres;

--
-- Name: query_grouping; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE query_grouping (
    id bigint NOT NULL,
    code character varying(255) NOT NULL
);


ALTER TABLE query_grouping OWNER TO postgres;

--
-- Name: query_variable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE query_variable (
    id bigint NOT NULL,
    code character varying(255) NOT NULL
);


ALTER TABLE query_variable OWNER TO postgres;

--
-- Name: tag; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tag (
    name character varying(255) NOT NULL
);


ALTER TABLE tag OWNER TO postgres;

--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "user" (
    username character varying(255) NOT NULL,
    agreenda boolean,
    apikey character varying(255),
    birthday character varying(255),
    city character varying(255),
    country character varying(255),
    email character varying(255),
    firstname character varying(255),
    fullname character varying(255),
    gender character varying(255),
    isactive boolean,
    lastname character varying(255),
    password character varying(255),
    phone character varying(255),
    picture character varying(255),
    team character varying(255),
    web character varying(255)
);


ALTER TABLE "user" OWNER TO postgres;

--
-- Name: user_languages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_languages (
    user_username character varying(255) NOT NULL,
    languages character varying(255)
);


ALTER TABLE user_languages OWNER TO postgres;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_roles (
    user_username character varying(255) NOT NULL,
    roles character varying(255)
);


ALTER TABLE user_roles OWNER TO postgres;

--
-- Name: value; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE value (
    code character varying(255) NOT NULL,
    label character varying(255)
);


ALTER TABLE value OWNER TO postgres;

--
-- Name: variable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE variable (
    code character varying(255) NOT NULL,
    description text,
    iscovariable boolean,
    isfilter boolean,
    isgrouping boolean,
    isvariable boolean,
    label character varying(255),
    length integer,
    maxvalue double precision,
    minvalue double precision,
    type character varying(255),
    units character varying(255),
    group_code character varying(255)
);


ALTER TABLE variable OWNER TO postgres;

--
-- Name: variable_value; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE variable_value (
    variable_code character varying(255) NOT NULL,
    values_code character varying(255) NOT NULL
);


ALTER TABLE variable_value OWNER TO postgres;

--
-- Name: vote; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE vote (
    id bigint NOT NULL,
    value integer NOT NULL,
    app_id integer,
    user_username character varying(255)
);


ALTER TABLE vote OWNER TO postgres;


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 1, false);


--
-- Name: app_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY app
    ADD CONSTRAINT app_pkey PRIMARY KEY (id);


--
-- Name: article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY article
    ADD CONSTRAINT article_pkey PRIMARY KEY (slug);


--
-- Name: config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY config
    ADD CONSTRAINT config_pkey PRIMARY KEY (id);


--
-- Name: config_title_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY config_title
    ADD CONSTRAINT config_title_pkey PRIMARY KEY (config_id, title_key);


--
-- Name: dataset_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dataset_data
    ADD CONSTRAINT dataset_data_pkey PRIMARY KEY (dataset_code, data_key);


--
-- Name: dataset_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dataset
    ADD CONSTRAINT dataset_pkey PRIMARY KEY (code);


--
-- Name: experiment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT experiment_pkey PRIMARY KEY (uuid);


--
-- Name: filter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY filter
    ADD CONSTRAINT filter_pkey PRIMARY KEY (id);


--
-- Name: group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "group"
    ADD CONSTRAINT group_pkey PRIMARY KEY (code);


--
-- Name: model_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY model
    ADD CONSTRAINT model_pkey PRIMARY KEY (slug);


--
-- Name: query_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query
    ADD CONSTRAINT query_pkey PRIMARY KEY (id);


--
-- Name: tag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (name);


--
-- Name: uk_39uwjlq8i49lng83ca3ksh4mg; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_group
    ADD CONSTRAINT uk_39uwjlq8i49lng83ca3ksh4mg UNIQUE (groups_code);


--
-- Name: uk_kyrua54629jbk1ir780vji7s3; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_filter
    ADD CONSTRAINT uk_kyrua54629jbk1ir780vji7s3 UNIQUE (filters_id);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (username);


--
-- Name: value_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY value
    ADD CONSTRAINT value_pkey PRIMARY KEY (code);


--
-- Name: variable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY variable
    ADD CONSTRAINT variable_pkey PRIMARY KEY (code);


--
-- Name: vote_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vote
    ADD CONSTRAINT vote_pkey PRIMARY KEY (id);


--
-- Name: fk_1fwrodma3jyox5kjqwpfegx1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_covariable
    ADD CONSTRAINT fk_1fwrodma3jyox5kjqwpfegx1f FOREIGN KEY (code) REFERENCES variable(code);


--
-- Name: fk_1kjpgxiphmdwn94d3wswywktw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT fk_1kjpgxiphmdwn94d3wswywktw FOREIGN KEY (user_username) REFERENCES "user"(username);


--
-- Name: fk_27affffd93lg54wkfi0eum3tm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY model
    ADD CONSTRAINT fk_27affffd93lg54wkfi0eum3tm FOREIGN KEY (config_id) REFERENCES config(id);


--
-- Name: fk_39uwjlq8i49lng83ca3ksh4mg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_group
    ADD CONSTRAINT fk_39uwjlq8i49lng83ca3ksh4mg FOREIGN KEY (groups_code) REFERENCES "group"(code);


--
-- Name: fk_3h0auvt8ej4raxwr7m4ua788f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY model
    ADD CONSTRAINT fk_3h0auvt8ej4raxwr7m4ua788f FOREIGN KEY (query_id) REFERENCES query(id);


--
-- Name: fk_4jo0ysx9u1qrunx8ilmty3dvb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_variable
    ADD CONSTRAINT fk_4jo0ysx9u1qrunx8ilmty3dvb FOREIGN KEY (id) REFERENCES query(id);


--
-- Name: fk_4kkhjrytsb3at05o5h7dqp2en; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_grouping
    ADD CONSTRAINT fk_4kkhjrytsb3at05o5h7dqp2en FOREIGN KEY (code) REFERENCES variable(code);


--
-- Name: fk_4m4cg0j1mtk42t739eer0l6ie; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY article_tag
    ADD CONSTRAINT fk_4m4cg0j1mtk42t739eer0l6ie FOREIGN KEY (article_slug) REFERENCES article(slug);


--
-- Name: fk_4yrfkom9tsbomi7ddq9yaaet6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vote
    ADD CONSTRAINT fk_4yrfkom9tsbomi7ddq9yaaet6 FOREIGN KEY (app_id) REFERENCES app(id);


--
-- Name: fk_55phec4qkjfaf40o26ainqh04; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_grouping
    ADD CONSTRAINT fk_55phec4qkjfaf40o26ainqh04 FOREIGN KEY (id) REFERENCES query(id);


--
-- Name: fk_6nlwicucyu66eo2crmm7faysc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dataset_variable
    ADD CONSTRAINT fk_6nlwicucyu66eo2crmm7faysc FOREIGN KEY (dataset_code) REFERENCES dataset(code);


--
-- Name: fk_728j4s15p4hjh1hgi69n6odht; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT fk_728j4s15p4hjh1hgi69n6odht FOREIGN KEY (model_slug) REFERENCES model(slug);


--
-- Name: fk_7amk7wobwicj4eu9kg27ath5d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY config_yaxisvariables
    ADD CONSTRAINT fk_7amk7wobwicj4eu9kg27ath5d FOREIGN KEY (config_id) REFERENCES config(id);


--
-- Name: fk_87oyy59jkinfuuav3fgg778b1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dataset_data
    ADD CONSTRAINT fk_87oyy59jkinfuuav3fgg778b1 FOREIGN KEY (dataset_code) REFERENCES dataset(code);


--
-- Name: fk_9qh6d61269tit9ylbiw1io9ss; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY model
    ADD CONSTRAINT fk_9qh6d61269tit9ylbiw1io9ss FOREIGN KEY (updatedby_username) REFERENCES "user"(username);


--
-- Name: fk_bbtsxjmsa5mb44u8bnhiru8tx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY variable
    ADD CONSTRAINT fk_bbtsxjmsa5mb44u8bnhiru8tx FOREIGN KEY (group_code) REFERENCES "group"(code);


--
-- Name: fk_cibo3wr7epeda4ihp4mk2uj3i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY group_group
    ADD CONSTRAINT fk_cibo3wr7epeda4ihp4mk2uj3i FOREIGN KEY (group_code) REFERENCES "group"(code);


--
-- Name: fk_cl2hlvgcj1eunkncaeg7gjmv2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY variable_value
    ADD CONSTRAINT fk_cl2hlvgcj1eunkncaeg7gjmv2 FOREIGN KEY (variable_code) REFERENCES variable(code);


--
-- Name: fk_dsqlojxvp4ugf7lfwkmtfyoyq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dataset_header
    ADD CONSTRAINT fk_dsqlojxvp4ugf7lfwkmtfyoyq FOREIGN KEY (dataset_code) REFERENCES dataset(code);


--
-- Name: fk_emu14hoeeji8n0dvgy0xe1w0m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "group"
    ADD CONSTRAINT fk_emu14hoeeji8n0dvgy0xe1w0m FOREIGN KEY (parent_code) REFERENCES "group"(code);


--
-- Name: fk_fxr8crpi28t12c5uis744oaf5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY config_title
    ADD CONSTRAINT fk_fxr8crpi28t12c5uis744oaf5 FOREIGN KEY (config_id) REFERENCES config(id);


--
-- Name: fk_gaanmaixpa38dt6rpkaclip0p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY model
    ADD CONSTRAINT fk_gaanmaixpa38dt6rpkaclip0p FOREIGN KEY (dataset_code) REFERENCES dataset(code);


--
-- Name: fk_hvivt77kdqt578xw1euc4ps2p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY article_tag
    ADD CONSTRAINT fk_hvivt77kdqt578xw1euc4ps2p FOREIGN KEY (tags_name) REFERENCES tag(name);


--
-- Name: fk_ixt1r76x6eb8571d1rejyrf6d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY filter_values
    ADD CONSTRAINT fk_ixt1r76x6eb8571d1rejyrf6d FOREIGN KEY (filter_id) REFERENCES filter(id);


--
-- Name: fk_kyrua54629jbk1ir780vji7s3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_filter
    ADD CONSTRAINT fk_kyrua54629jbk1ir780vji7s3 FOREIGN KEY (filters_id) REFERENCES filter(id);


--
-- Name: fk_l1ltbtlvoof7ff3rpk33tkygi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_variable
    ADD CONSTRAINT fk_l1ltbtlvoof7ff3rpk33tkygi FOREIGN KEY (code) REFERENCES variable(code);


--
-- Name: fk_l31vqnacpxbb2jmp326n9enmw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY variable_value
    ADD CONSTRAINT fk_l31vqnacpxbb2jmp326n9enmw FOREIGN KEY (values_code) REFERENCES value(code);


--
-- Name: fk_lfvyrnhv3soykf3kodgnkx74o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_languages
    ADD CONSTRAINT fk_lfvyrnhv3soykf3kodgnkx74o FOREIGN KEY (user_username) REFERENCES "user"(username);


--
-- Name: fk_mm0iove438omw0pl3xdlxsa6l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY filter
    ADD CONSTRAINT fk_mm0iove438omw0pl3xdlxsa6l FOREIGN KEY (variable_code) REFERENCES variable(code);


--
-- Name: fk_mq1vuehgh2leq8g9dtdp0l6nq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY model
    ADD CONSTRAINT fk_mq1vuehgh2leq8g9dtdp0l6nq FOREIGN KEY (createdby_username) REFERENCES "user"(username);


--
-- Name: fk_op53w7utnopm4ec3j1003bk8v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_filter
    ADD CONSTRAINT fk_op53w7utnopm4ec3j1003bk8v FOREIGN KEY (query_id) REFERENCES query(id);


--
-- Name: fk_piooelhlqdg5ecwucse8jmpyx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY experiment
    ADD CONSTRAINT fk_piooelhlqdg5ecwucse8jmpyx FOREIGN KEY (createdby_username) REFERENCES "user"(username);


--
-- Name: fk_px2lsjf6f4aajnrno4e3qiiav; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dataset_grouping
    ADD CONSTRAINT fk_px2lsjf6f4aajnrno4e3qiiav FOREIGN KEY (dataset_code) REFERENCES dataset(code);


--
-- Name: fk_qlchk27g13xbd68uy13gwmkp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY query_covariable
    ADD CONSTRAINT fk_qlchk27g13xbd68uy13gwmkp FOREIGN KEY (id) REFERENCES query(id);


--
-- Name: fk_qqo7ebj6u7gaymh4055f3cqcu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY article
    ADD CONSTRAINT fk_qqo7ebj6u7gaymh4055f3cqcu FOREIGN KEY (updatedby_username) REFERENCES "user"(username);


--
-- Name: fk_sj9qxyo9ebe3rs9schj6cv15l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY article
    ADD CONSTRAINT fk_sj9qxyo9ebe3rs9schj6cv15l FOREIGN KEY (createdby_username) REFERENCES "user"(username);


--
-- Name: fk_tjxqe691ce40h4cafbek3l27q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vote
    ADD CONSTRAINT fk_tjxqe691ce40h4cafbek3l27q FOREIGN KEY (user_username) REFERENCES "user"(username);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--
