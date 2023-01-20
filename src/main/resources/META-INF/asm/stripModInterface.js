var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var ic2ApiPackage = 'ic2/api/';

function initializeCoreMod() {
    return {
        'stripModInterface': {
            'target': {
                'type': 'CLASS',
                'names': function() {
                    return [
                        'dev.su5ed.gtexperimental.item.ElectricItem',
                        'dev.su5ed.gtexperimental.item.ElectricToolItem',
                        'dev.su5ed.gtexperimental.item.ElectricArmorItem',
                        'dev.su5ed.gtexperimental.block.BaseEntityBlock',
                        'dev.su5ed.gtexperimental.item.AdvancedDrillItem',
                        'dev.su5ed.gtexperimental.item.AdvancedWrenchItem',
                        'dev.su5ed.gtexperimental.item.WrenchItem',
                        'dev.su5ed.gtexperimental.item.BugSprayItem',
                        'dev.su5ed.gtexperimental.item.ReactorCoolantCellItem',
                        'dev.su5ed.gtexperimental.item.NuclearFuelRodItem'
                    ]
                }
            },
            'transformer': function (classNode) {
                var ic2Loaded = ASMAPI.getSystemPropertyFlag('gtexperimental.ic2_loaded');
                var it = classNode.interfaces.iterator();
                while (it.hasNext()) {
                    var name = it.next().replace('.', '/');
                    if (!ic2Loaded && name.startsWith(ic2ApiPackage)) {
                        ASMAPI.log('DEBUG', 'Stripping interface ' + name + ' from class ' + classNode.name);
                        it.remove();
                    }
                }
                return classNode;
            }
        }
    }
}