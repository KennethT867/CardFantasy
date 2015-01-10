package cfvbaibai.cardfantasy.engine.skill;

import cfvbaibai.cardfantasy.data.Skill;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.SkillEffect;
import cfvbaibai.cardfantasy.engine.SkillEffectType;
import cfvbaibai.cardfantasy.engine.SkillResolver;
import cfvbaibai.cardfantasy.engine.SkillUseInfo;

public class BasicAtBuff extends RemovableBuff {
    public static void apply(SkillResolver resolver, SkillUseInfo skillUseInfo, CardInfo card) {
        Skill skill = skillUseInfo.getSkill();
        int adjAT = skill.getImpact() * card.getInitAT() / 1000;
        resolver.getStage().getUI().useSkill(card, skill, true);
        resolver.getStage().getUI().adjustAT(skillUseInfo.getOwner(), card, adjAT, skill);
        card.addEffect(new SkillEffect(SkillEffectType.ATTACK_CHANGE, skillUseInfo, adjAT, false));
    }
}
