// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    mainSidebar: [
        'index',
        {
            type: 'category',
            label: '通用材料系统',
            collapsible: false,
            link: {
                type: 'doc',
                id: 'material/index',
            },
            items: [
                'material/element',
            ],
        },
        {
            type: 'category',
            label: '通用机器系统',
            collapsible: false,
            link: {
                type: 'doc',
                id: 'machine/index',
            },
            items: [
                "machine/phase-energy",
                {
                    type: 'category',
                    label: '多方块机器系统',
                    collapsible: false,
                    link: {
                        type: 'doc',
                        id: 'machine/multiblock/index',
                    },
                    items: [],
                },
            ],
        },
        "quick-start",
        "api-reference",
        "world-gen",
    ],
};

export default sidebars;
