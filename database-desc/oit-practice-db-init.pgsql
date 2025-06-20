CREATE TABLE users (
	id UUID PRIMARY KEY,
	username VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	password_hash VARCHAR(255) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	last_login_at TIMESTAMP,
	is_active BOOLEAN NOT NULL DEFAULT FALSE,
	reputation INT NOT NULL DEFAULT 0,
	avatar_url VARCHAR,
	is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE user_logs (
	id UUID PRIMARY KEY,
	user_id UUID NOT NULL,
	action_desc VARCHAR NOT NULL,
	action_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE user_comments (
	id UUID PRIMARY KEY,
	author_id UUID NOT NULL,
	target_id UUID NOT NULL,
	comment_text TEXT NOT NULL,
	reputation INT NOT NULL CHECK (reputation BETWEEN -1 AND 1) DEFAULT 0,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (author_id) REFERENCES users(id),
	FOREIGN KEY (target_id) REFERENCES users(id)
);
CREATE TABLE roles (
	id UUID PRIMARY KEY,
	role_name VARCHAR UNIQUE NOT NULL
);
CREATE TABLE users_roles (
	user_id UUID NOT NULL,
	role_id UUID NOT NULL,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (role_id) REFERENCES roles(id)
);
CREATE TABLE activities (
	id UUID PRIMARY KEY,
	activity_name VARCHAR(255) UNIQUE NOT NULL
);
CREATE TABLE tags (
	id UUID PRIMARY KEY,
	tag_name VARCHAR(50) UNIQUE NOT NULL
);
CREATE TABLE forms (
	id UUID PRIMARY KEY,
	activity_id UUID NOT NULL,
	title VARCHAR(255) NOT NULL,
	form_text TEXT NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_complete BOOLEAN NOT NULL DEFAULT FALSE,
	FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE
);
CREATE TABLE forms_tags (
	form_id UUID NOT NULL,
	tag_id UUID NOT NULL,
	PRIMARY KEY (form_id, tag_id),
	FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE,
	FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);
CREATE TABLE users_forms (
	user_id UUID NOT NULL,
	form_id UUID NOT NULL,
	is_leader BOOLEAN NOT NULL,
	PRIMARY KEY(user_id, form_id),
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE
);
CREATE INDEX idx_user_logs_user_id ON user_logs(user_id);
CREATE INDEX idx_user_comments_author_id ON user_comments(author_id);
CREATE INDEX idx_user_comments_target_id ON user_comments(target_id);
CREATE INDEX idx_users_roles_user_id ON users_roles(user_id);
CREATE INDEX idx_users_roles_role_id ON users_roles(role_id);
CREATE INDEX idx_forms_activity_id ON forms(activity_id);
CREATE INDEX idx_forms_tags_form_id ON forms_tags(form_id);
CREATE INDEX idx_forms_tags_tag_id ON forms_tags(tag_id);
CREATE INDEX idx_users_forms_user_id ON users_forms(user_id);
CREATE INDEX idx_users_forms_form_id ON users_forms(form_id);