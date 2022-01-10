/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.model.base;

/**
 * This class defines all classes (ex : Human fighter, darkFighter...) that a player can chose.<BR><BR>
 * 
 * Data :<BR><BR>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId or null if this class is the root</li><BR><BR>
 * 
 * @version $Revision: 1.4.4.4 $ $Date: 2005/03/27 15:29:33 $
 * 
 */
public enum ClassId
{
    fighter(0, false, Race.Human, null),

    warrior(1, false, Race.Human, fighter), gladiator(2, false, Race.Human, warrior), warlord(
            3, false, Race.Human, warrior), knight(4, false, Race.Human, fighter), paladin(5,
            false, Race.Human, knight), darkAvenger(6, false, Race.Human, knight), rogue(7, false,
            Race.Human, fighter), treasureHunter(8, false, Race.Human, rogue), hawkeye(9, false,
            Race.Human, rogue),

    mage(10, true, Race.Human, null), wizard(11, true, Race.Human, mage), sorceror(12, true,
            Race.Human, wizard), necromancer(13, true, Race.Human, wizard), warlock(14, true,
            Race.Human, wizard), cleric(15, true, Race.Human, mage), bishop(16, true, Race.Human,
            cleric), prophet(17, true, Race.Human, cleric),

    elvenFighter(18, false, Race.Elf, null), elvenKnight(19, false, Race.Elf, elvenFighter), templeKnight(
            20, false, Race.Elf, elvenKnight), swordSinger(21, false, Race.Elf, elvenKnight), elvenScout(
            22, false, Race.Elf, elvenFighter), plainsWalker(23, false, Race.Elf, elvenScout), silverRanger(
            24, false, Race.Elf, elvenScout),

    elvenMage(25, true, Race.Elf, null), elvenWizard(26, true, Race.Elf, elvenMage), spellsinger(
            27, true, Race.Elf, elvenWizard), elementalSummoner(28, true, Race.Elf, elvenWizard), oracle(
            29, true, Race.Elf, elvenMage), elder(30, true, Race.Elf, oracle),

    darkFighter(31, false, Race.DarkElf, null), palusKnight(32, false, Race.DarkElf, darkFighter), shillienKnight(
            33, false, Race.DarkElf, palusKnight), bladedancer(34, false, Race.DarkElf, palusKnight), assassin(
            35, false, Race.DarkElf, darkFighter), abyssWalker(36, false, Race.DarkElf, assassin), phantomRanger(
            37, false, Race.DarkElf, assassin),

    darkMage(38, true, Race.DarkElf, null), darkWizard(39, true, Race.DarkElf, darkMage), spellhowler(
            40, true, Race.DarkElf, darkWizard), phantomSummoner(41, true, Race.DarkElf, darkWizard), shillienOracle(
            42, true, Race.DarkElf, darkMage), shillienElder(43, true, Race.DarkElf, shillienOracle),

    orcFighter(44, false, Race.Orc, null), orcRaider(45, false, Race.Orc, orcFighter), destroyer(
            46, false, Race.Orc, orcRaider), orcMonk(47, false, Race.Orc, orcFighter), tyrant(48,
            false, Race.Orc, orcMonk),

    orcMage(49, false, Race.Orc, null), orcShaman(50, true, Race.Orc, orcMage), overlord(51, true,
            Race.Orc, orcShaman), warcryer(52, true, Race.Orc, orcShaman),

    dwarvenFighter(53, false, Race.Dwarf, null), scavenger(54, false, Race.Dwarf, dwarvenFighter), bountyHunter(
            55, false, Race.Dwarf, scavenger), artisan(56, false, Race.Dwarf, dwarvenFighter), warsmith(
            57, false, Race.Dwarf, artisan),

    /*
     * Dummy Entries
     * <START>
     */
    dummyEntry1(58, false, null, null), dummyEntry2(59, false, null, null), dummyEntry3(60, false, null,
            null), dummyEntry4(61, false, null, null), dummyEntry5(62, false, null, null), dummyEntry6(
            63, false, null, null), dummyEntry7(64, false, null, null), dummyEntry8(65, false, null,
            null), dummyEntry9(66, false, null, null), dummyEntry10(67, false, null, null), dummyEntry11(
            68, false, null, null), dummyEntry12(69, false, null, null), dummyEntry13(70, false, null,
            null), dummyEntry14(71, false, null, null), dummyEntry15(72, false, null, null), dummyEntry16(
            73, false, null, null), dummyEntry17(74, false, null, null), dummyEntry18(75, false, null,
            null), dummyEntry19(76, false, null, null), dummyEntry20(77, false, null, null), dummyEntry21(
            78, false, null, null), dummyEntry22(79, false, null, null), dummyEntry23(80, false, null,
            null), dummyEntry24(81, false, null, null), dummyEntry25(82, false, null, null), dummyEntry26(
            83, false, null, null), dummyEntry27(84, false, null, null), dummyEntry28(85, false, null,
            null), dummyEntry29(86, false, null, null), dummyEntry30(87, false, null, null),
    /*
     * <END>
     * Of Dummy entries
     */

    /*
     * Now the bad boys! new class ids :)) (3rd classes)
     */
    duelist(88, false, Race.Human, gladiator), dreadnought(89, false, Race.Human, warlord), phoenixKnight(
            90, false, Race.Human, paladin), hellKnight(91, false, Race.Human, darkAvenger), sagittarius(
            92, false, Race.Human, hawkeye), adventurer(93, false, Race.Human, treasureHunter), archmage(
            94, true, Race.Human, sorceror), soultaker(95, true, Race.Human, necromancer), arcanaLord(
            96, true, Race.Human, warlock), cardinal(97, true, Race.Human, bishop), hierophant(98,
            true, Race.Human, prophet),

    evaTemplar(99, false, Race.Elf, templeKnight), swordMuse(100, false, Race.Elf, swordSinger), windRider(
            101, false, Race.Elf, plainsWalker), moonlightSentinel(102, false, Race.Elf, silverRanger), mysticMuse(
            103, true, Race.Elf, spellsinger), elementalMaster(104, true, Race.Elf, elementalSummoner), evaSaint(
            105, true, Race.Elf, elder),

    shillienTemplar(106, false, Race.DarkElf, shillienKnight), spectralDancer(107, false,
            Race.DarkElf, bladedancer), ghostHunter(108, false, Race.DarkElf, abyssWalker), ghostSentinel(
            109, false, Race.DarkElf, phantomRanger), stormScreamer(110, true, Race.DarkElf,
            spellhowler), spectralMaster(111, true, Race.DarkElf, phantomSummoner), shillienSaint(112,
            true, Race.DarkElf, shillienElder),

    titan(113, false, Race.Orc, destroyer), grandKhauatari(114, false, Race.Orc, tyrant), dominator(
            115, true, Race.Orc, overlord), doomcryer(116, true, Race.Orc, warcryer),

    fortuneSeeker(117, false, Race.Dwarf, bountyHunter), maestro(118, false, Race.Dwarf, warsmith);

    /** The Identifier of the Class */
    private final int _id;

    /** True if the class is a mage class */
    private final boolean _isMage;

    /** The Race object of the class */
    private final Race _race;

    /** The parent ClassId or null if this class is a root */
    private final ClassId _parent;

    /**
     * Constructor of ClassId.<BR><BR>
     * @param pId 
     * @param pIsMage 
     * @param pRace 
     * @param pParent 
     */
    private ClassId(int pId, boolean pIsMage, Race pRace, ClassId pParent)
    {
        _id = pId;
        _isMage = pIsMage;
        _race = pRace;
        _parent = pParent;
    }

    /**
     * Return the Identifier of the Class.<BR><BR>
     * @return 
     */
    public final int getId()
    {
        return _id;
    }

    /**
     * Return True if the class is a mage class.<BR><BR>
     * @return 
     */
    public final boolean isMage()
    {
        return _isMage;
    }

    /**
     * Return the Race object of the class.<BR><BR>
     * @return 
     */
    public final Race getRace()
    {
        return _race;
    }

    /**
     * Return True if this Class is a child of the selected ClassId.<BR><BR>
     * 
     * @param cid The parent ClassId to check
     * @return 
     * 
     */
    public final boolean childOf(ClassId cid)
    {
        if (_parent == null)
        {
        	return false;
        }

        if (_parent == cid)
        {
        	return true;
        }

        return _parent.childOf(cid);

    }
    
    /**
     * Return True if this Class is equal to the selected ClassId or a child of the selected ClassId.<BR><BR>
     * 
     * @param cid The parent ClassId to check
     * @return 
     * 
     */
    public final boolean equalsOrChildOf(ClassId cid)
    {
        return this == cid || childOf(cid);
    }

    /**
     * Return the child level of this Class (0=root, 1=child level 1...).<BR><BR>
     * 
     * @return 
     * 
     */
    public final int level()
    {
        if (_parent == null)
        {
        	return 0;
        }

        return 1 + _parent.level();
    }

    /**
     * Return its parent ClassId<BR><BR>
     * @return 
     * 
     */
    public final ClassId getParent()
    {
        return _parent;
    }
}
