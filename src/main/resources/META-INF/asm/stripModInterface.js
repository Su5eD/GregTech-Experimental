var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var ic2ApiPackage = 'ic2/api/';

function initializeCoreMod() {
    return {
        'stripModInterface': {
            'target': {
                'type': 'CLASS',
                'names': function() {
                    return [
                        'dev.su5ed.gregtechmod.item.ElectricItem',
                        'dev.su5ed.gregtechmod.item.ElectricToolItem',
                        'dev.su5ed.gregtechmod.block.BaseEntityBlock',
                        'dev.su5ed.gregtechmod.item.AdvancedDrillItem',
                        'dev.su5ed.gregtechmod.item.AdvancedWrenchItem',
                        'dev.su5ed.gregtechmod.item.WrenchItem',
                        'dev.su5ed.gregtechmod.item.BugSprayItem',
                        'dev.su5ed.gregtechmod.item.ReactorCoolantCellItem',
                        'dev.su5ed.gregtechmod.item.NuclearFuelRodItem',
                    ]
                }
            },
            'transformer': function (classNode) {
                var ic2Loaded = ASMAPI.getSystemPropertyFlag('gregtechmod.ic2_loaded');
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