create table market(
                       id bigserial constraint market_pkey primary key,
                       code varchar,
                       symbol varchar,
                       name varchar,
                       country varchar,
                       website varchar
);

create table instrument(
                           id bigserial constraint instrument_pkey primary key,
                           symbol varchar not null,
                           full_name varchar,
                           simple_name varchar,
                           market_id bigint
);