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
      items: [],
    },
  ],
};

export default sidebars;
