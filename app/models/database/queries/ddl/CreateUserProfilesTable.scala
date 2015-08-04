package models.database.queries.ddl

import models.database.Statement

case object CreateUserProfilesTable extends Statement {
  override val sql = """
    create table user_profiles (
      provider character varying(64) not null,
      key text not null,
      email character varying(256),
      first_name character varying(512),
      last_name character varying(512),
      full_name character varying(512),
      avatar_url character varying(512),
      created timestamp not null,
      constraint pk_user_profiles primary key (provider, key)
    ) with (oids=false);

    create index user_profiles_email_idx on user_profiles using btree (email collate pg_catalog."default");
  """
}
