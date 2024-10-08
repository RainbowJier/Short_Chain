<template>
  <div class="header">访问地区</div>
  <div class="map">
    <div ref="chart" class="echarts"></div>
  </div>
</template>
<script setup>
import { ref, onMounted, defineProps } from 'vue'
import * as echarts from 'echarts';
import 'echarts/extension/bmap/bmap'
import mapEchartData from './mapEchartData.json'
const { list } = defineProps(['list'])
const chart = ref(null)

const init = () => {
  const myChart = echarts.init(chart.value);
  const data = list
  const geoCoordMap = JSON.parse(JSON.stringify(mapEchartData))
  const convertData = function (data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
      var geoCoord = geoCoordMap[data[i].name];
      if (geoCoord) {
        res.push({
          name: data[i].name,
          value: geoCoord.concat(data[i].value)
        });
      }
    }
    return res;
  };
  const renderItem = (params, api) => {
    var coords = [
      [116.7, 39.53],
      [103.73, 36.03],
      [112.91, 27.87],
      [120.65, 28.01],
      [119.57, 39.95]
    ];
    var points = [];
    for (var i = 0; i < coords.length; i++) {
      points.push(api.coord(coords[i]));
    }
    var color = api.visual('color');
    return {
      type: 'polygon',
      shape: {
        points: echarts.graphic.clipPointsByRect(points, {
          x: params.coordSys.x,
          y: params.coordSys.y,
          width: params.coordSys.width,
          height: params.coordSys.height
        })
      },
      style: api.style({
        fill: color,
        stroke: echarts.color.lift(color)
      })
    };
  }
  const option = {
    backgroundColor: 'transparent',
    title: {
      text: '全国城市用户访问分布',
      left: 'center',
      textStyle: {
        color: '#fff'
      }
    },
    tooltip: {
      trigger: 'item'
    },
    bmap: {
      center: [130.114129, -70.550339],
      zoom: 4,
      roam: true,
      mapStyle: {
        styleJson: [
          {
            featureType: 'water',
            elementType: 'all',
            stylers: {
              color: '#044161'
            }
          },
          {
            featureType: 'land',
            elementType: 'all',
            stylers: {
              color: '#004981'
            }
          },
          {
            featureType: 'boundary',
            elementType: 'geometry',
            stylers: {
              color: '#064f85'
            }
          },
          {
            featureType: 'railway',
            elementType: 'all',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'highway',
            elementType: 'geometry',
            stylers: {
              color: '#004981'
            }
          },
          {
            featureType: 'highway',
            elementType: 'geometry.fill',
            stylers: {
              color: '#005b96',
              lightness: 1
            }
          },
          {
            featureType: 'highway',
            elementType: 'labels',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'arterial',
            elementType: 'geometry',
            stylers: {
              color: '#004981'
            }
          },
          {
            featureType: 'arterial',
            elementType: 'geometry.fill',
            stylers: {
              color: '#00508b'
            }
          },
          {
            featureType: 'poi',
            elementType: 'all',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'green',
            elementType: 'all',
            stylers: {
              color: '#056197',
              visibility: 'off'
            }
          },
          {
            featureType: 'subway',
            elementType: 'all',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'manmade',
            elementType: 'all',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'local',
            elementType: 'all',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'arterial',
            elementType: 'labels',
            stylers: {
              visibility: 'off'
            }
          },
          {
            featureType: 'boundary',
            elementType: 'geometry.fill',
            stylers: {
              color: '#029fd4'
            }
          },
          {
            featureType: 'building',
            elementType: 'all',
            stylers: {
              color: '#1a5787'
            }
          },
          {
            featureType: 'label',
            elementType: 'all',
            stylers: {
              visibility: 'off'
            }
          }
        ]
      }
    },
    series: [
      {
        name: '访问量',
        type: 'scatter',
        coordinateSystem: 'bmap',
        data: convertData(data),
        encode: {
          value: 2
        },
        symbolSize: function (val) {
          return val[2] / 3;
        },
        label: {
          formatter: '{b}',
          position: 'right'
        },
        itemStyle: {
          color: '#f4e925'
        },
        emphasis: {
          label: {
            show: true
          }
        }
      },
      {
        name: 'Top 5',
        type: 'effectScatter',
        coordinateSystem: 'bmap',
        data: convertData(
          data
            .sort(function (a, b) {
              return b.value - a.value;
            })
            .slice(0, 5)
        ),
        encode: {
          value: 2
        },
        symbolSize: function (val) {
          return val[2] / 3;
        },
        showEffectOn: 'emphasis',
        rippleEffect: {
          brushType: 'stroke'
        },
        hoverAnimation: true,
        label: {
          formatter: '{b}',
          position: 'right',
          show: true
        },
        itemStyle: {
          color: '#f4e925',
          shadowBlur: 10,
          shadowColor: '#333'
        },
        zlevel: 1
      },
      {
        type: 'custom',
        coordinateSystem: 'bmap',
        renderItem: renderItem,
        itemStyle: {
          opacity: 0.5
        },
        animation: false,
        silent: true,
        data: [0],
        z: -10
      }
    ]
  };
  option && myChart.setOption(option);
}

onMounted(() => {
  init()
})
</script>
<style lang='less' scoped>
.echarts {
  width: 800px;
  height: 600px;
}

.header {
  margin-bottom: 30px;
  font-size: 16px;
  font-weight: bold;
  color: #000;
  border-bottom: 1px solid #f0f0f0;
}

.map {
  display: flex;
  justify-content: space-between;
}
</style>