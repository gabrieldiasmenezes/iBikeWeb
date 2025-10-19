-- Inserir roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_FUNCIONARIO');

-- Inserir usuários (substitua $2a$10$EXAMPLE_BCRYPT_HASH pelo BCrypt real)
INSERT INTO administrador (cpf, nm_adm, email, password, status, id_patio) VALUES
('00000000000', 'Admin Principal', 'admin@ibike.com', '$2a$12$kGuBCaYqxsuXk8HfMXJeiepeJge5ZAAAZBsCC7mFb.v9onZ4R/pEy', 'ADMIN', 1),
('11111111111', 'Funcionario Comum', 'funcionario@ibike.com', '$2a$12$kGuBCaYqxsuXk8HfMXJeiepeJge5ZAAAZBsCC7mFb.v9onZ4R/pEy', 'FUNCIONARIO', 2);
