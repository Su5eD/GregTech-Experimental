package dev.su5ed.gtexperimental.api.machine;

/**
 * Provides information for Energy Control's Information Panel
 */
public interface IPanelInfoProvider {
    /**
     * Is the {@link net.minecraft.world.level.block.entity.BlockEntity BlockEntity} providing information at the moment?
     */
    boolean isGivingInformation();

    /**
     * @return the first line displayed on the Information Panel
     */
    String getMainInfo();

    /**
     * @return the second line displayed on the Information Panel
     */
    String getSecondaryInfo();

    /**
     * @return the third line displayed on the Information Panel
     */
    String getTertiaryInfo();
}
