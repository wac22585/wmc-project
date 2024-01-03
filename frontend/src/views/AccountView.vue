<template>
    <v-layout>
        <NavBar />
        <v-main class="main">
            <h1>Account here!</h1>
        </v-main>
    </v-layout>
</template>

<script setup>
    import NavBar from '@/components/Navigation-Bar.vue';
</script>

<script>
    export default {
        components: {
            NavBar,
        }
    }
</script>

<style>
    .main {
        margin-top: 70px;
    }
</style>