package br.com.empresa.healthcheckteam.backend.defaultdata;

import java.util.HashMap;
import java.util.Map;

public class DefaultDataGenerator {

    public static final Map<String, String[]> defaultQuestions = new HashMap<String, String[]>() {{
        put("Motivação e Propósito: O time está motivado? As pessoas estão engajadas? As pessoas entendem o propósito do seu trabalho? Sinto que...",
                new String[]{"O time está desmotivado",
                        "O time quase nunca está motivado",
                        "Há pouca motivação no time",
                        "O time está motivado para algumas tarefas",
                        "Em geral, o time está motivado. Sabemos nosso propósito",
                        "O time está altamente motivado e engajado no propósito"});
        put("Empoderamento e Autonomia do time: o time tem autonomia para decidir no que é mais importante para trabalhar no momento? O time consegue atuar em bloqueios e riscos? O time depende de outros times para fazer seu trabalho? O time se responsabiliza pelos acertos e erros? Sinto que...",
                new String[]{"Nunca temos autonomia",
                        "Quase nunca temos autonomia",
                        "Às vezes, temos autonomia",
                        "Temos autonomia, mas dependemos de outras pessoas para decidir",
                        "Podemos decidir a maioria das coisas (temos pouca dependência)",
                        "Podemos decidir qualquer coisa (inclusive qualitividade)"});
        put("Satisfação do Cliente: Você acha que o seu trabalho atende as necessidades do cliente? O cliente está feliz com as entregas? Sinto que...",
                new String[]{"Não entregamos valor para o cliente",
                        "Há pouca entrega de valor para o cliente",
                        "O cliente gosta de algumas entregas",
                        "Entregamos valor para o cliente",
                        "O cliente está satisfeito",
                        "Nossas entregas Encantam o cliente"});
        put("Colaboração do Time: Trabalho frequente junto com alguém da equipe (exemplo: pair programming), com revezamento de colegas e tarefas? O time está engajado em trocar informação e ajudar um ao outro? Pode-se contar com os membros do time para auxílio? Sinto que...",
                new String[]{"O time não se conversa",
                        "Quase nunca há colaboração",
                        "Há alguma colaboração no time",
                        "Há troca de informação, mas não podemos contar com isso",
                        "O time colabora bastante entre si",
                        "Podemos contar com qualquer membro do time"});
        put("Excelência técnica: As pessoas com quem trabalho mandam bem? Sinto que...",
                new String[]{"O time não tem a skill técnica necessária",
                        "Skill técnica do time é muito baixa",
                        "O time ainda não tem a skill técnica necessária",
                        "A skill técnica do time é suficiente para desenvolver o trabalho",
                        "O time manda bem!",
                        "O time é o conselho jedi no desenvolvimento de software!"});
        put("Desenvolvimento Ágil: Fazemos as cerimônias que precisamos para termos entregas com qualidade? Usamos as cerimônias para fazer a melhoria contínua?Daily, Retrospectiva, Ronda, Pair Programming, Code Review, Alfa cruzado/Demonstração da Feature, Refactoring, Testes Automatizados. Sinto que...",
                new String[]{"O time não faz as cerimônias",
                        "O time faz apenas a Daily e nem é diária",
                        "O time faz o mínimo possível das cerimônias",
                        "O time faz cerimônias suficiente para ter visibilidade e qualidade",
                        "O time faz as cerimônias e usa o resultado para melhorar continuamente",
                        "O time está sempre se reinventando"});
        put("Processo Objective: O time conhece e segue o processo Objective?Obs: Quebra das tarefas em 3 níveis, orçamento por complexidade, retroalimentação do T-Shirt, indicadores, WIP Limitado, Fluxo contínuo. Sinto que...",
                new String[]{"Não temos processo",
                        "Temos um processo a ser seguido, mas não é muito claro",
                        "Seguimos algumas coisas do processo",
                        "Seguimos boa parte do processo. WIP limitado no 3 nível",
                        "Seguimos grande parte do processo. WIP Limitado no 2 nível",
                        "Estamos em Estado de Fluxo!!"});
        put("Entrega Contínua: Commitamos continuamente nosso código? Temos sempre um build “pronto” para produção? Meus commits entram automaticamente em produção? Sinto que...",
                new String[]{"Geração do build e entrega são manuais. Commit direto na trunk",
                        "Trabalhamos em branch antigas. Trunk com testes falhando",
                        "Fazemos downmerge diário. Build ainda é manual",
                        "Integração contínua. Entrega é feita manualmente ao cliente",
                        "Integração e Entrega para homologação/produção estão automatizados",
                        "Cada commit que faço na trunk vai direto para homologação / produção"});
        put("Qualitividade: Temos “slack” focado em melhoria de código? Atuamos ativamente para diminuir a complexidade do produto? Nossas ações melhoram a produtividade? Obs: automatizar mecanismos periféricos não é qualitividade. Sinto que...",
                new String[]{"Não fazemos nada para melhorar produtividade",
                        "Temos iniciativas isoladas de melhoria de qualidade",
                        "10% do nosso tempo é dedicado a melhorar código",
                        "15% no nosso tempo é dedicado a melhorar o código",
                        "20% no nosso tempo é dedicado a melhorar o código",
                        "Qualitividade é um processo contínuo executado a cada Feature"});
    }};

}
