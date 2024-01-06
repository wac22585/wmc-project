<template>
    <v-container>
        <button @click="back" class="btn-back">
            <v-icon size="40">mdi-arrow-left</v-icon>
        </button>
        <div class="title">Edit User</div>
        <div class="subtitle">manage {{ user.firstname }} {{ user.lastname }}</div>
        <Edit />
    </v-container>
</template>

<script setup>
    import axios from 'axios';
    import Edit from '@/components/EditDialog.vue';
</script>

<script>
    export default {
        data() {
            return {
                user: {},
                id: this.$route.params.id,
            }
        },
        async mounted() {
            try {
                const response = await axios.get(`users/get/${this.id}`);
                if(response.status == 200) {
                    this.user = response.data;
                }
                else {
                    console.log(response.data)
                }
            } catch(error) {
                alert(error)
            }
        },
        methods: {
            back() {
                this.$router.push({ name: 'Home' });
            },
        },
    }
</script>

<style scoped>
    .title {
    color: black;
    font-size: 30px;
  }

  .subtitle {
    color: black;
    font-size: 20px;
    margin-top: -10px;
    margin-bottom: 30px;
  }

  .btn-back {
    margin-bottom: 20px;
  }
</style>