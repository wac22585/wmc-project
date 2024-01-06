<template>
    <label for="">Firstname</label>
    <v-text-field v-model="user.firstname" append-inner-icon="mdi-pencil"  variant="underlined"></v-text-field>
    <label for="">Lastname</label>
    <v-text-field v-model="user.lastname" append-inner-icon="mdi-pencil"  variant="underlined"></v-text-field>
    <label for="">Email address</label>
    <v-text-field append-inner-icon="mdi-pencil" v-model="user.email" variant="underlined"></v-text-field>

    <label for="">Phone-Number</label>
    <v-text-field append-inner-icon="mdi-pencil" v-model="user.phoneNumber" variant="underlined"></v-text-field>

    <label for="">Roles</label>
    <v-select
        :items="roles"
        item-title="name"
        item-value="id"
        v-model="selectedRoles"
        bg-color="none"
        density="compact"
        variant="plain"
        multiple
        class="select"
    >
        <template v-slot:selection="{item, index}">
            <v-chip v-if="index < 2">
                <span>{{ item.title }}</span>
            </v-chip>
            <span 
            v-if="index === 2"
            class="text-grey text-caption align-self-center"
            >
                (+{{ selectedRoles.length - 2 }} others)
            </span>
        </template>
    </v-select>
    <DatePicker :defaultDate="computedDefaultDate" :border="true"/>

    <div class="btn-div">
        <Button :useWhiteBackground="true" label="Delete"  @click="deleteUser"/>
        <Button label="Save" @click="save"/>
    </div>
</template>

<script setup>
    import axios from 'axios';
    import DatePicker from '@/components/DatePicker.vue';
    import Button from '@/components/Button.vue';
</script>

<script>   
    export default {
        data() {
            return {
                roles: [],
                user: {},
                id: this.$route.params.id,
                selectedRoles: [],
            }
        },
        computed: {
            computedDefaultDate() {
                let date = new Date(this.user.birthdate);
                return new Date(date.getFullYear(), date.getMonth(), date.getDate())
            }
        },
        async mounted() {
            try{
            } catch(error) {
                alert(error);
            }
            try {
                const response = await axios.get(`users/get/${this.id}`);
                if(response.status == 200) {
                    this.user = response.data;
                }
                else {
                    console.log(response.data)
                }
               
                const roles = await axios.get('roles/all');
                this.roles = roles.data;
                this.selectedRoles = this.user.roles.map(userRoleName => {
                    let matchingRole = this.roles.find(role => role.name === userRoleName);
                    return matchingRole ? matchingRole.id : null;
                }).filter(id => id !== null);
            } catch(error) {
                alert(error);
            }
        },
        methods: {
            async deleteUser() {
                try {
                    await axios.put(`users/delete/${this.id}`);
                    this.$router.push({ name: 'Home' });
                } catch (error) {
                    alert(error);
                }
            },
            async save() {
                try {  
                    const updatedUserData = {
                        firstname: this.user.firstname,
                        lastname: this.user.lastname,
                        email: this.user.email,
                        phoneNumber: this.user.phoneNumber,
                        birthdate: this.user.birthdate,
                        roles: this.selectedRoles.map(roleId => {
                            const role = this.roles.find(r => r.id === roleId);
                            return role ? role.name : null;
                        }).filter(roleName => roleName !== null)
                    };
                    console.log(updatedUserData)
                    const response = await axios.put(`users/update/${this.id}`, this.user);
                    if (response.status === 200) {
                        this.$router.push({ name: 'Home' });
                    } else {
                       alert(response.data)
                    }
                } catch (error) {
                  alert(error)
                }
            }
        }
    }
</script>

<style scoped>
    .btn-div {
        display: flex;
        justify-content: end;
    }

    .select {
        border-bottom: #707070 1px solid;
        background: white !important;
        height: 40px;
        margin-bottom: 20px;
        min-width: 300px;
        padding-inline: 10px;
        display: block;
    }
</style>