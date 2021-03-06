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
package net.sf.l2j.gameserver.templates;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2Object;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Summon;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Env;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;
import net.sf.l2j.gameserver.skills.funcs.Func;
import net.sf.l2j.gameserver.skills.funcs.FuncTemplate;

/**
 * This class contains all informations concerning the item (weapon, armor, etc).<BR>
 * Mother class of :
 * <LI>L2Armor</LI>
 * <LI>L2EtcItem</LI>
 * <LI>L2Weapon</LI> 
 * @version $Revision: 1.7.2.2.2.5 $ $Date: 2005/04/06 18:25:18 $
 */
public abstract class L2Item
{
	public static final int TYPE1_WEAPON_RING_EARRING_NECKLACE = 0;
	public static final int TYPE1_SHIELD_ARMOR = 1;
	public static final int TYPE1_ITEM_QUESTITEM_ADENA = 4;

	public static final int TYPE2_WEAPON = 0;
	public static final int TYPE2_SHIELD_ARMOR = 1;
	public static final int TYPE2_ACCESSORY = 2;
	public static final int TYPE2_QUEST = 3;
	public static final int TYPE2_MONEY = 4;
    public static final int TYPE2_OTHER = 5;
    public static final int TYPE2_PET_WOLF = 6;
    public static final int TYPE2_PET_HATCHLING = 7;
    public static final int TYPE2_PET_STRIDER = 8;

	public static final int SLOT_NONE = 0x0000;
	public static final int SLOT_UNDERWEAR = 0x0001;
	public static final int SLOT_R_EAR = 0x0002;
	public static final int SLOT_L_EAR = 0x0004;
	public static final int SLOT_NECK = 0x0008;
	public static final int SLOT_R_FINGER = 0x0010;
	public static final int SLOT_L_FINGER = 0x0020;
	public static final int SLOT_HEAD = 0x0040;
	public static final int SLOT_R_HAND = 0x0080;
	public static final int SLOT_L_HAND = 0x0100;
	public static final int SLOT_GLOVES = 0x0200;
	public static final int SLOT_CHEST = 0x0400;
	public static final int SLOT_LEGS = 0x0800;
	public static final int SLOT_FEET = 0x1000;
	public static final int SLOT_BACK = 0x2000;
	public static final int SLOT_LR_HAND = 0x4000;
	public static final int SLOT_FULL_ARMOR = 0x8000;
    public static final int SLOT_HAIR = 0x010000;
    public static final int SLOT_WOLF = 0x020000;
    public static final int SLOT_HATCHLING = 0x040000;
    public static final int SLOT_STRIDER = 0x080000;
	
	public static final int MATERIAL_STEEL = 0x00; // ??
	public static final int MATERIAL_FINE_STEEL = 0x01; // ??
	public static final int MATERIAL_BLOOD_STEEL = 0x02; // ??
	public static final int MATERIAL_BRONZE = 0x03; // ??
	public static final int MATERIAL_SILVER = 0x04; // ??
	public static final int MATERIAL_GOLD = 0x05; // ??
	public static final int MATERIAL_MITHRIL = 0x06; // ??
	public static final int MATERIAL_ORIHARUKON = 0x07; // ??
	public static final int MATERIAL_PAPER = 0x08; // ??
	public static final int MATERIAL_WOOD = 0x09; // ??
	public static final int MATERIAL_CLOTH = 0x0a; // ??
	public static final int MATERIAL_LEATHER = 0x0b; // ??
	public static final int MATERIAL_BONE = 0x0c; // ??
	public static final int MATERIAL_HORN = 0x0d; // ??
	public static final int MATERIAL_DAMASCUS = 0x0e; // ??
	public static final int MATERIAL_ADAMANTAITE = 0x0f; // ??
	public static final int MATERIAL_CHRYSOLITE = 0x10; // ??
	public static final int MATERIAL_CRYSTAL = 0x11; // ??
	public static final int MATERIAL_LIQUID = 0x12; // ??
	public static final int MATERIAL_SCALE_OF_DRAGON = 0x13; // ??
	public static final int MATERIAL_DYESTUFF = 0x14; // ??
	public static final int MATERIAL_COBWEB = 0x15; // ??
    public static final int MATERIAL_SEED = 0x15; // ??

	public static final int CRYSTAL_NONE = 0x00; // ??
	public static final int CRYSTAL_D = 0x01; // ??
	public static final int CRYSTAL_C = 0x02; // ??
	public static final int CRYSTAL_B = 0x03; // ??
	public static final int CRYSTAL_A = 0x04; // ??
	public static final int CRYSTAL_S = 0x05; // ??
	
    public static final int[] crystalItemId = {0, 1458, 1459, 1460, 1461, 1462};
    public static final int[] crystalEnchantBonusArmor = {0, 11, 6, 11, 19, 25};
    public static final int[] crystalEnchantBonusWeapon = {0, 90, 45, 67, 144, 250};

	private final int _itemId;
	private final String _name;
	private final int _type1;	// needed for item list (inventory)
	private final int _type2;	// different lists for armor, weapon, etc
	private final int _weight;
	private final boolean _crystallizable;
	private final boolean _stackable;
	private final int _materialType;
	private final int _crystalType; // default to none-grade
	private final int _bodyPart;
	private final int _referencePrice;
	private final int _crystalCount;
	private final boolean _sellable;
    private final boolean _dropable;
    private final boolean _destroyable;
    private final boolean _tradeable;
    
    private final boolean _heroItem;
	
	protected final Enum<?> _type;
	
	protected FuncTemplate[] _funcTemplates;
	protected List<Condition> _preConditions;
	protected EffectTemplate[] _effectTemplates;
    protected L2Skill[] _skills;
    
    private static final Func[] _emptyFunctionSet = new Func[0];
    protected static final L2Effect[] _emptyEffectSet = new L2Effect[0];
	
	/**
	 * Constructor of the L2Item that fill class variables.<BR><BR>
	 * <U><I>Variables filled :</I></U><BR>
	 * <LI>type</LI>
	 * <LI>_itemId</LI>
	 * <LI>_name</LI>
	 * <LI>_type1 & _type2</LI>
	 * <LI>_weight</LI>
	 * <LI>_crystallizable</LI>
	 * <LI>_stackable</LI>
	 * <LI>_materialType & _crystalType & _crystlaCount</LI>
	 * <LI>_durability</LI>
	 * <LI>_bodypart</LI>
	 * <LI>_referencePrice</LI>
	 * <LI>_sellable</LI>
	 * @param type : Enum designating the type of the item
	 * @param set : StatsSet corresponding to a set of couples (key,value) for description of the item
	 */
	protected L2Item(Enum<?> type, StatsSet set)
	{
		_type = type;
		_itemId = set.getInteger("item_id");
		_name   = set.getString("name");
		_type1  = set.getInteger("type1");	// needed for item list (inventory)
		_type2  = set.getInteger("type2");	// different lists for armor, weapon, etc
		_weight = set.getInteger("weight");
		_crystallizable = set.getBool("crystallizable");
		_stackable      = set.getBool("stackable", false);
		_materialType   = set.getInteger("material");
		_crystalType    = set.getInteger("crystal_type", CRYSTAL_NONE); // default to none-grade 
		_bodyPart       = set.getInteger("bodypart");
		_referencePrice = set.getInteger("price");
		_crystalCount   = set.getInteger("crystal_count", 0);
		_sellable       = set.getBool("sellable", true);
        _dropable       = set.getBool("dropable", true);
        _destroyable    = set.getBool("destroyable", true);
        _tradeable      = set.getBool("tradeable", true);
        _heroItem = (_itemId >= 6611 && _itemId <= 6621) || _itemId == 6842;
	}
	
	/**
	 * Returns the itemType.
	 * @return Enum
	 */
	public Enum<?> getItemType()
	{
		return _type;
	}

	/**
	 * Returns the ID of the item.
	 * @return int
	 */
	public final int getItemId()
	{
		return _itemId;
	}

	public abstract int getItemMask();
	
	/**
	 * Return the type of material of the item
	 * @return int
	 */
	public final int getMaterialType()
	{
		return _materialType;
	}

	/**
	 * Returns the type 2 of the item
	 * @return int
	 */
	public final int getType2()
	{
		return _type2;
	}

	/**
	 * Returns the weight of the item
	 * @return int
	 */
	public final int getWeight()
	{
		return _weight;
	}

	/**
	 * Returns if the item is crystallizable
	 * @return boolean
	 */
	public final boolean isCrystallizable()
	{
		return _crystallizable;
	}

	/**
	 * Return the type of crystal if item is crystallizable
	 * @return int
	 */
	public final int getCrystalType()
	{
		return _crystalType;
	}

    /**
     * Return the type of crystal if item is crystallizable
     * @return int
     */
    public final int getCrystalItemId()
    {
        return crystalItemId[_crystalType];
    }
    
	/**
	 * Returns the grade of the item.<BR><BR>
	 * <U><I>Concept :</I></U><BR>
	 * In fact, this function returns the type of crystal of the item.
	 * @return int
	 */
    public final int getItemGrade()
    {
        return getCrystalType();
    }
    
    /**
     * Returns the quantity of crystals for crystallization
     * @return int
     */
	public final int getCrystalCount()
    {
		return _crystalCount;
	}

    /**
     * Returns the quantity of crystals for crystallization on specific enchant level
     * @param enchantLevel 
     * @return
     */
    public final int getCrystalCount(int enchantLevel)
    {
        if (enchantLevel > 3) switch(_type2)
        {
            case TYPE2_SHIELD_ARMOR: case TYPE2_ACCESSORY: return _crystalCount + crystalEnchantBonusArmor[getCrystalType()] * (3*enchantLevel -6);
            case TYPE2_WEAPON: return _crystalCount + crystalEnchantBonusWeapon[getCrystalType()] * (2*enchantLevel -3);
            default: return _crystalCount;
        }
        else if (enchantLevel > 0) switch(_type2)
        {
            case TYPE2_SHIELD_ARMOR: case TYPE2_ACCESSORY: return _crystalCount + crystalEnchantBonusArmor[getCrystalType()] * enchantLevel;
            case TYPE2_WEAPON: return _crystalCount + crystalEnchantBonusWeapon[getCrystalType()] * enchantLevel;
            default: return _crystalCount;
        }
        else return _crystalCount;
    }

	/**
	 * Returns the name of the item
	 * @return String
	 */
	public final String getName()
	{
		return _name;
	}

	/**
	 * Return the part of the body used with the item.
	 * @return int
	 */
	public final int getBodyPart()
	{
		return _bodyPart;
	}

	/**
	 * Returns the type 1 of the item
	 * @return int
	 */
	public final int getType1()
	{
		return _type1;
	}

	/**
	 * Returns if the item is stackable
	 * @return boolean
	 */
	public final boolean isStackable()
	{
		return _stackable;
	}

    /**
     * Returns if the item is consumable
     * @return boolean
     */
    public boolean isConsumable()
    {
        return false;
    }
    
    /**
     * Returns if the item is a mercenary posting ticket
     * @return boolean
     */
    public boolean isMercenaryTicket()
    {
        return false;
    }

	/**
	 * Returns the price of reference of the item
	 * @return int
	 */
	public final int getReferencePrice()
	{
        return (isConsumable() ? (int)(_referencePrice * Config.RATE_CONSUMABLE_COST) : _referencePrice);
	}
	
	/**
	 * Returns if the item can be sold
	 * @return boolean
	 */
	public final boolean isSellable()
	{
		return _sellable;
	}

    /**
     * Returns if the item can be dropped
     * @return boolean
     */
    public final boolean isDropable()
    {
        return _dropable;
    }

    /**
     * Returns if the item can be destroyed
     * @return boolean
     */
    public final boolean isDestroyable()
    {
        return _destroyable;
    }

    /**
     * Returns if the item can be traded
     * @return boolean
     */
    public final boolean isTradeable()
    {
        return _tradeable;
    }

    /**
	 * Returns if item is hero-only
	 * @return
	 */
	public final boolean isHeroItem()
	{
		return _heroItem;
	}
    
    /**
     * Returns if item is for hatchling
     * @return boolean
     */
    public boolean isForHatchling()
    {
        return (_type2 == TYPE2_PET_HATCHLING);
    }

    /**
     * Returns if item is for strider
     * @return boolean
     */
    public boolean isForStrider()
    {
        return (_type2 == TYPE2_PET_STRIDER);
    }
	
    /**
     * Returns if item is for wolf
     * @return boolean
     */
    public boolean isForWolf()
    {
        return (_type2 == TYPE2_PET_WOLF);
    }

    /**
	 * Returns array of Func objects containing the list of functions used by the item 
	 * @param instance : L2ItemInstance pointing out the item
	 * @param player : L2Character pointing out the player
	 * @return Func[] : array of functions
	 */
    public Func[] getStatFuncs(L2ItemInstance instance, L2Character player)
    {
		if (_funcTemplates == null)
			return _emptyFunctionSet;
    	List<Func> funcs = new ArrayList<>();
		for (FuncTemplate t : _funcTemplates)
		{
	    	Env env = new Env();
	    	env.player = player;
	    	env.target = player;
	    	Func f = t.getFunc(env, this); // Skill is owner
	    	if (f != null)
	    		funcs.add(f);
		}
    	if (funcs.size() == 0)
    		return _emptyFunctionSet;
    	return funcs.toArray(new Func[funcs.size()]);
    }
    
    /**
     * Returns the effects associated with the item.
     * @param instance : L2ItemInstance pointing out the item
     * @param player : L2Character pointing out the player
     * @return L2Effect[] : array of effects generated by the item
     */
    public L2Effect[] getEffects(L2ItemInstance instance, L2Character player)
    {
		if (_effectTemplates == null)
			return _emptyEffectSet;
    	List<L2Effect> effects = new ArrayList<>();
		for (EffectTemplate et : _effectTemplates)
        {
	    	Env env = new Env();
	    	env.player = player;
	    	env.target = player;
	    	L2Effect e = et.getEffect(env, this);
	    	if (e != null)
	    		effects.add(e);
		}
    	if (effects.size() == 0)
    		return _emptyEffectSet;
    	return effects.toArray(new L2Effect[effects.size()]);
    }
    
    /**
     * Returns effects of skills associated with the item.
     * @param caster : L2Character pointing out the caster
     * @param target : L2Character pointing out the target
     * @return L2Effect[] : array of effects generated by the skill
     */
    public L2Effect[] getSkillEffects(L2Character caster, L2Character target)
    {
        if (_skills == null)
            return _emptyEffectSet;
        List<L2Effect> effects = new ArrayList<>();

        for (L2Skill skill : _skills)
        {
            if (!skill.checkCondition(caster, target, true)) 
                continue; // Skill condition not met

            if (target.getFirstEffect(skill.getId()) != null)
                target.removeEffect(target.getFirstEffect(skill.getId()));
            for (L2Effect e : skill.getEffects(caster, target))
                effects.add(e);
        }
        if (effects.size() == 0)
            return _emptyEffectSet;
        return effects.toArray(new L2Effect[effects.size()]);
    }
    
    
    /**
     * Add the FuncTemplate f to the list of functions used with the item
     * @param f : FuncTemplate to add
     */
    public void attach(FuncTemplate f)
    {
    	// If _functTemplates is empty, create it and add the FuncTemplate f in it
    	if (_funcTemplates == null)
    	{
    		_funcTemplates = new FuncTemplate[]{f};
    	}
    	else
    	{
    		int len = _funcTemplates.length;
    		FuncTemplate[] tmp = new FuncTemplate[len+1];
    		// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
    		// number of components to be copied)
    		System.arraycopy(_funcTemplates, 0, tmp, 0, len);
    		tmp[len] = f;
    		_funcTemplates = tmp;
    	}
    }

    /**
     * Add the EffectTemplate effect to the list of effects generated by the item
     * @param effect : EffectTemplate
     */
    public void attach(EffectTemplate effect)
    {
    	if (_effectTemplates == null)
    	{
    		_effectTemplates = new EffectTemplate[]{effect};
    	}
    	else
    	{
    		int len = _effectTemplates.length;
    		EffectTemplate[] tmp = new EffectTemplate[len+1];
    		// Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
    		// number of components to be copied)
    		System.arraycopy(_effectTemplates, 0, tmp, 0, len);
    		tmp[len] = effect;
    		_effectTemplates = tmp;
    	}
    }

    /**
     * Add the L2Skill skill to the list of skills generated by the item
     * 
     * @param skill : L2Skill
     */
    public void attach(L2Skill skill)
    {
        if (_skills == null)
        {
            _skills = new L2Skill[]{skill};
        }
        else
        {
            int len = _skills.length;
            L2Skill[] tmp = new L2Skill[len+1];
            // Definition : arraycopy(array source, begins copy at this position of source, array destination, begins copy at this position in dest,
            // number of components to be copied)
            System.arraycopy(_skills, 0, tmp, 0, len);
            tmp[len] = skill;
            _skills = tmp;
        }
    }
    
    public final void attach(Condition c)
	{
		if (_preConditions == null)
		{
			_preConditions = new ArrayList<>(1);
		}
		if (!_preConditions.contains(c))
		{
			_preConditions.add(c);
		}
	}

    public boolean checkCondition(L2Character activeChar, L2Object target, boolean sendMessage)
	{
    	if (activeChar instanceof L2PcInstance)
    	{
    		if (activeChar.isGM() && ((L2PcInstance)activeChar).getAccessLevel() >= Config.GM_ITEM_RESTRICTION)
    		{
    			return true;
    		}
    	}

		if (_preConditions == null || _preConditions.isEmpty())
		{
			return true;
		}
		
		Env env = new Env();
		env.player = activeChar;
		env.target = target != null && target instanceof L2Character ? (L2Character)target : null;
		
		for (Condition preCondition : _preConditions)
		{
			if (preCondition == null)
				return true;

			if (!preCondition.test(env, this))
			{
				if (activeChar instanceof L2Summon)
				{
					activeChar.getActingPlayer().sendPacket(new SystemMessage(SystemMessage.PET_CANNOT_USE_ITEM));
					return false;
				}

				if (sendMessage)
				{
					String msg = preCondition.getMessage();
					int msgId = preCondition.getMessageId();
					if (msg != null)
					{
						activeChar.sendMessage(msg);
					}
					else if (msgId != 0)
					{
						SystemMessage sm = new SystemMessage(msgId);
						if (preCondition.isAddName())
						{
							sm.addItemName(_itemId);
						}
						activeChar.sendPacket(sm);
					}
				}
				return false;
			}
		}
		return true;
	}
    
    /**
     * Returns the name of the item
     * @return String
     */
	@Override
	public String toString()
	{
		return _name;
	}
}
