package com.fiap.ponabri.config;

/**
 * Documentação sobre a lógica de roles no sistema.
 * 
 * - Novos usuários recebem a role padrão "USER" automaticamente no endpoint de registro.
 * - A role é armazenada no campo "role" da entidade Usuario, que é persistida no banco de dados.
 * - Alterações nas roles dos usuários podem ser feitas diretamente no banco de dados, sem necessidade de alteração no código.
 * - O sistema não sobrescreve a role do usuário após o registro, permitindo flexibilidade para ajustes manuais.
 */
public class RoleDocumentation {
    // Esta classe é apenas para documentação e não contém lógica executável.
}
