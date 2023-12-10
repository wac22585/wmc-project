<template>
    <v-btn class="btn">{{ content }}</v-btn>
</template>

<script>
    export default {
        data() {
           
        },
        props: {
            content: String,
        },
    }
</script>

<style scoped>
    .btn {
        margin: 5px;
        
    }
</style>