--
-- Name: query_covariable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE query_training_datasets (
    id bigint NOT NULL,
    code character varying(255) NOT NULL
);


ALTER TABLE query_training_datasets OWNER TO postgres;

ALTER TABLE ONLY query_training_datasets
    ADD CONSTRAINT fk_query_training_datasets FOREIGN KEY (code) REFERENCES variable(code);


CREATE TABLE query_testing_datasets (
    id bigint NOT NULL,
    code character varying(255) NOT NULL
);


ALTER TABLE query_testing_datasets OWNER TO postgres;

ALTER TABLE ONLY query_testing_datasets
    ADD CONSTRAINT fk_query_testing_datasets FOREIGN KEY (code) REFERENCES variable(code);

CREATE TABLE query_validation_datasets (
    id bigint NOT NULL,
    code character varying(255) NOT NULL
);


ALTER TABLE query_validation_datasets OWNER TO postgres;

ALTER TABLE ONLY query_validation_datasets
    ADD CONSTRAINT fk_query_validation_datasets FOREIGN KEY (code) REFERENCES variable(code);
