package model;

import java.util.HashSet;
import java.util.Set;

/**
 * It keeps track of which skins have been purchased and which are still available for purchase.
 */
public final class ShopModel {
    private final Set<String> purchasedSkins = new HashSet<>();
    private final Set<String> toBuySkins = new HashSet<>();

    /**
     * Creates a new shop model with the default initial state.
     */
    public ShopModel() {
        CoinStorage.loadTotalCoins();
        purchasedSkins.add(SkinId.DEFAULT.toString());
        initializeSkinState(SkinId.SHARK);
        initializeSkinState(SkinId.PURPLE);
        initializeSkinState(SkinId.GHOST);
    }

    /**
     * Checks whether the given skin is still available for purchase.
     *
     * @param id the identifier of the skin
     * @return {@code true} if the skin has not been purchased yet and can be bought, {@code false} otherwise
     */
    public boolean isToBuy(final SkinId id) {
        return toBuySkins.contains(id.toString());
    }

    /**
     * Checks whether the given skin has already been purchased.
     *
     * @param id id the identifier of the skin
     * @return {@code true} if the skin has been purchased, {@code false} otherwise
     */
    public boolean isPurchased(final SkinId id) {
        return purchasedSkins.contains(id.toString());
    }

    /**
     * The skin is removed from the list of purchasable skins and added to the list of owned skins.
     *
     * @param id id the identifier of the skin
     */
    public void markPurchased(final SkinId id) {
        toBuySkins.remove(id.toString());
        purchasedSkins.add(id.toString());
        CoinStorage.markSkinPurchased(id);
    }

    private void initializeSkinState(final SkinId id) {
        if (CoinStorage.isSkinPurchased(id)) {
            purchasedSkins.add(id.toString());
        } else {
            toBuySkins.add(id.toString());
        }
    }

    /**
     * Enumeration of all available player skins.
     */
    public enum SkinId {
        DEFAULT, SHARK, PURPLE, GHOST
    }
}
