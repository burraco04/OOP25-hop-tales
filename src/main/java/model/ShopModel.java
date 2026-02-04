package model;

import java.util.HashSet;
import java.util.Set;

public final class ShopModel {
    private final Set<String> purchasedSkins = new HashSet<>();
    private final Set<String> toBuySkins = new HashSet<>();

    public ShopModel() {
        purchasedSkins.add(SkinId.DEFAULT.name());
        toBuySkins.add(SkinId.SHARK.name());
        toBuySkins.add(SkinId.PURPLE.name());
        toBuySkins.add(SkinId.GHOST.name());
    }

    public boolean isToBuy(final SkinId id) {
        return toBuySkins.contains(id.name());
    }

    public boolean isPurchased(final SkinId id) {
        return purchasedSkins.contains(id.name());
    }

    public void markPurchased(final SkinId id) {
        toBuySkins.remove(id.name());
        purchasedSkins.add(id.name());
    }

    public enum SkinId {
        DEFAULT, SHARK, PURPLE, GHOST
    }
}
