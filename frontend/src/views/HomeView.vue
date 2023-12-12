<template>
  <v-container>
    <div class="title">Users</div>
    <div class="subtitle">view and manage users</div>
    <div class="add-btns">
     <InputField width="700px" label="Search" class="inputfield"> </InputField>
     <v-divider vertical class="mx-2"></v-divider>
     <Button content="Filter" />
     <Button content="Add" @click="add"/>
    </div>
    <div>
      <v-table>
        <thead>
          <tr>
            <th class="text-left table-head">User</th>
            <th class="table-head">Created</th>
            <th class="text-left table-head">Role</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="user in users"
            :key="user.id"
          >
            <td> <span class="username">{{ user.firstname }} {{ user.lastname }}</span> <br> <span>{{ user.email }}</span></td>
            <td>{{ new Date(user.created).toLocaleDateString("de-DE") }}</td>
            <td>{{ user.roles }}</td>
            <td>
              <v-menu location="end">
                <template v-slot:activator="{ props }">
                  <button class="btn-more" v-bind="props"><svg-icon type="mdi" :path="path"></svg-icon></button>
                </template>
                <div class="menu" elevation="3" transition="false">
                  <v-btn variant="text" @click="remove(user.id)">Delete</v-btn>
                </div>
              </v-menu>
            </td>
          </tr>
        </tbody>
      </v-table>
    </div>
  </v-container>
</template>

<script setup> 
  import axios from 'axios';
  import InputField from '@/components/Input.vue';
  import Button from '@/components/Button-Settings.vue'
  import SvgIcon from '@jamescoyle/vue-icon';
  import { mdiDotsHorizontal } from '@mdi/js';
</script>

<script>
  export default {
    components: {
      SvgIcon,
      InputField,
      Button,
    },
      data() {
          return {
              users: [],
              path: mdiDotsHorizontal,
          };
      },
      async mounted() {
          try {
              const response = await axios.get("users/all");
              console.log(response);
              this.users = response.data;
          }
          catch(e)
          {
              alert(e)
          }
      },
      methods: {
        add() {
          this.$router.push({ name: 'Add' });
        },
        async remove(id) {
          try {
            console.log("id:", id)
            const response = await axios.put(`users/delete/${id}`);

            if (response.data === 'OK') {
              this.users = this.users.filter(user => user.id !== id);
            } else {
              console.error('Error deleting user:', response.data);
            }
          } catch (e) {
            alert(e);
          }
        }
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
  }

  .table-head {
    color: #707070;
  }

  .username {
    font-weight: bold;
  }

  .btn-more {
    border-radius: 50%;
    padding: 2px;
  }

  .inputfield {
    display: inline-block;
  }

  .menu {
    border: solid 1px #707070;
    border-radius: 10px;
    margin-right: 10px;
  }
</style>