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
            slug: "/material",
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
            slug: "/machine",
            items: [
                "machine/1-phase-energy",
                {
                    type: 'category',
                    label: '多方块机器系统',
                    collapsible: false,
                    link: {
                        type: 'doc',
                        id: 'machine/multiblock/index',
                    },
                    slug: "/machine/multiblock",
                },
            ],
        },
        "1-quick-start",
        "2-api-reference",
        "3-world-gen",
    ],
};

export default sidebars;
