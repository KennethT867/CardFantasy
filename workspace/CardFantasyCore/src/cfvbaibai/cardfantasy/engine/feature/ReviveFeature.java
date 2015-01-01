package cfvbaibai.cardfantasy.engine.feature;

import cfvbaibai.cardfantasy.CardFantasyRuntimeException;
import cfvbaibai.cardfantasy.data.Feature;
import cfvbaibai.cardfantasy.data.SkillTag;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.CardStatusItem;
import cfvbaibai.cardfantasy.engine.FeatureInfo;
import cfvbaibai.cardfantasy.engine.FeatureResolver;
import cfvbaibai.cardfantasy.engine.Grave;
import cfvbaibai.cardfantasy.engine.HeroDieSignal;

public final class ReviveFeature {
    public static void apply(FeatureResolver resolver, FeatureInfo featureInfo, CardInfo reviver) throws HeroDieSignal {
        if (reviver == null) {
            throw new CardFantasyRuntimeException("reviver should not be null");
        }
        Grave grave = reviver.getOwner().getGrave();
        boolean hasRevivableCard = false;
        for (CardInfo deadCard : grave.toList()) {
            if (deadCard != null && !deadCard.containsUsableFeaturesWithTag(SkillTag.复活)) {
                hasRevivableCard = true;
                break;
            }
        }
        if (!hasRevivableCard) {
            return;
        }
        Feature feature = featureInfo.getFeature();
        // Grave is a stack, find the last-in card and revive it.
        CardInfo cardToRevive = null;
        while (true) {
            CardInfo deadCard = resolver.getStage().getRandomizer().pickRandom(
                grave.toList(), 1, true, null).get(0);
            if (!deadCard.containsUsableFeaturesWithTag(SkillTag.复活)) {
                cardToRevive = deadCard;
                break;
            }
        }

        if (cardToRevive == null) {
            return;
        }
        resolver.getStage().getUI().useSkill(reviver, cardToRevive, feature, true);
        reviver.getOwner().getGrave().removeCard(cardToRevive);
        resolver.summonCard(reviver.getOwner(), cardToRevive, reviver);
        CardStatusItem item = CardStatusItem.weak(featureInfo);
        resolver.getStage().getUI().addCardStatus(reviver, cardToRevive, feature, item);
        cardToRevive.addStatus(item);
    }
}
