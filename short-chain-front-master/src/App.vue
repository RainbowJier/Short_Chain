<template>
  <div class="out_bg">
    <Header />
    <router-view></router-view>
  </div>
</template>

<script setup>
import Header from './components/Header.vue'
</script>

<style>
.out_bg {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
