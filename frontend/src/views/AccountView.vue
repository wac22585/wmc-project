<template>
    <v-layout>
        <NavBar />
        <v-main class="d-flex align-center justify-center">
            <v-container style="margin-top: 70px; margin-right: 40px;">
                <EditDialog />
            </v-container>
        </v-main>
    </v-layout>
</template>

<script setup>
    import NavBar from '@/components/Navigation-Bar.vue';
    import EditDialog from '@/components/EditDialog.vue';
</script>

<script>
    export default {
        components: {
            NavBar,
            EditDialog,
        }
    }
</script>

<style>
    .main {
        margin-top: 70px;
    }
</style>